package com.example.b1esimageweb.service;

import com.example.b1esimageweb.Exceptions.*;
import com.example.b1esimageweb.model.Album;
import com.example.b1esimageweb.model.Gallery;
import com.example.b1esimageweb.model.Photo;
import com.example.b1esimageweb.model.User;
import com.example.b1esimageweb.repository.AlbumRepository;
import com.example.b1esimageweb.repository.PhotoRepository;
import com.example.b1esimageweb.web.dto.AlbumDto;
import com.example.b1esimageweb.web.dto.PhotoDto;
import com.microsoft.azure.storage.CloudStorageAccount;
import com.microsoft.azure.storage.StorageException;
import com.microsoft.azure.storage.blob.CloudBlob;
import com.microsoft.azure.storage.blob.CloudBlobClient;
import com.microsoft.azure.storage.blob.CloudBlobContainer;
import org.apache.tomcat.util.http.fileupload.ByteArrayOutputStream;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.naming.NameNotFoundException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.security.InvalidKeyException;
import java.util.*;

@Service
public class AlbumService {

    private AlbumRepository albumRepository;
    private PhotoRepository photoRepository;
    private CloudBlobContainer container;
    private CloudStorageAccount account;
    private CloudBlobClient serviceClient;

    public AlbumService(AlbumRepository albumRepository, PhotoRepository photoRepository, @Value("${azure.storage.conection.string}") String storageConnectionAzure, @Value("${azure.storage.container.name}") String nameContainer) {
        this.albumRepository = albumRepository;
        this.photoRepository = photoRepository;
        try {
            account = CloudStorageAccount.parse(storageConnectionAzure);
            serviceClient = account.createCloudBlobClient();
            container = serviceClient.getContainerReference(nameContainer);
        } catch (InvalidKeyException | URISyntaxException e) {
            e.printStackTrace();
        } catch (StorageException e) {
            e.printStackTrace();
        }
    }

    public Album createAlbum(AlbumDto albumDto) throws NameNotFoundException {
        Album album = new Album();
        String albumName = albumDto.getName();
        String albumDescription = albumDto.getDescription();
        if(albumName != null) {
            album.setAlbumName(albumName);
            album.setDescription(albumDescription != null ? albumDescription : "");
            Photo pho = createPhoto(albumDto.getCoverPhoto());
            pho.addAlbum(album);
            Object obj = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            User currentUser = null;
            if(obj instanceof User){
                currentUser = (User) obj;
            }
            if(currentUser != null) {
                album.setUser(currentUser);
            }else{
                throw new UserNotFoundException("User does not exists");
            }
        }
        else {
            throw new NameNotFoundException("Album name not found");
        }
        return albumRepository.save(album);
    }

    public Map<Integer, List<PhotoDto>> getAllAlbumsForUser(User user){
        Map<Integer, List<PhotoDto>> albumPhotos = new HashMap<>();
        Iterable<Album> allAlbums = albumRepository.findAllByUser(user);
        for (Album album : allAlbums){
            List<PhotoDto> photos = new ArrayList<>();
            Iterable<Photo> allPhotos = photoRepository.findByAlbumsContaining(album);
            for(Photo photo : allPhotos){
                CloudBlob blob;
                try {
                    blob = container.getBlockBlobReference(photo.getPhotoId().toString());
                    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                    blob.download(outputStream);
                    byte[] photoContent = outputStream.toByteArray();
                    PhotoDto photoDto = new PhotoDto(photoContent, photo.getPhotoId(), photo.getGallery(), photo.getPhotoName(), photo.getAlbums(), photo.getPhotoExtension(), photo.getPhotoDescription());
                    photos.add(photoDto);
                } catch (URISyntaxException | StorageException e) {
                    e.printStackTrace();
                    return null;
                }
            }
            albumPhotos.put(album.getAlbumId(), photos);
        }
        return albumPhotos;
    }


    public Album getAlbumById(int albumId){
        return albumRepository.findById(albumId).orElseThrow(() -> new UserNotFoundException("Album with id " + albumId + " not found"));
    }

    public Iterable<PhotoDto> getPhotosByAlbum(Album album) {
        List<PhotoDto> photos = new ArrayList<>();

        for (Photo photo : photoRepository.findByAlbumsContaining(album)) {
            CloudBlob blob;
            try {
                blob = container.getBlockBlobReference(photo.getPhotoId().toString());
                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                blob.download(outputStream);
                byte[] photoContent = outputStream.toByteArray();
                photos.add(new PhotoDto(photoContent, photo.getPhotoId(), null, photo.getPhotoName(), photo.getAlbums(), photo.getPhotoExtension(), photo.getPhotoDescription()));
            } catch (URISyntaxException | StorageException e) {
                e.printStackTrace();
                return null;
            }
        }
        return photos;
    }

    public Album addPhotosToAlbum(int albumId, List<MultipartFile> photos){
        Album album = albumRepository.findById(albumId)
                .orElseThrow(() -> new AlbumNotFoundException("Album not found"));
        Gallery gallery = album.getUser().getGallery();

        for (MultipartFile photoFile : photos) {
            try {
                Photo p = createPhoto(photoFile);
                p.addAlbum(album);
                p.setGallery(gallery);
                photoRepository.save(p);
            } catch (Exception e) {
                throw new PhotoStorageException("Could not store photo");
            }
        }
        return album;
    }

    public Album addPhotosToAlbumFromGallery(int albumId, Iterable<Integer> photoIds){
        Album album = albumRepository.findById(albumId).orElseThrow(() -> new UserNotFoundException("Album with id " + albumId + " not found"));
        
        for (int photoId : photoIds) {
            Photo photo =  photoRepository.findById(photoId).orElseThrow(()-> new PhotoNotFoundException("Photo with id " + photoId + "not found"));  
            photo.addAlbum(album);
            photoRepository.save(photo);
        }

        return album;
    }

    public List<Integer> checkAlbumForPhotos(Album album, Iterable<Integer> photoIds) {
        List<Integer> photoIdsRepited = new ArrayList<>();
        for (int photoId : photoIds) {
            Photo photo = photoRepository.findById(photoId).orElseThrow(() -> new PhotoNotFoundException("Photo with id " + photoId + " not found"));
            if (photo.getAlbums().contains(album)) {
                photoIdsRepited.add(photo.getPhotoId());
            }
        }
        return photoIdsRepited; 
    }

    private Photo createPhoto(MultipartFile photo){
        Photo newPhoto = new Photo();

        String fileName = photo.getOriginalFilename();
        int lastDotIndex = fileName.lastIndexOf(".");
        String extension = fileName.substring(lastDotIndex + 1);
        newPhoto.setPhotoName(fileName);
        newPhoto.setPhotoExtension(extension);
        photoRepository.save(newPhoto);
        CloudBlob blob;
        try {
            blob = container.getBlockBlobReference(newPhoto.getPhotoId().toString());
            byte[] decodedBytes = photo.getBytes();
            blob.uploadFromByteArray(decodedBytes, 0, decodedBytes.length);
        } catch (URISyntaxException | StorageException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return photoRepository.save(newPhoto);
    }

    public void updateAlbum(int albumId, AlbumDto albumDto) throws NameNotFoundException {
        Album album = albumRepository.findById(albumId)
                .orElseThrow(() -> new AlbumNotFoundException("Album not found"));

        String albumName = albumDto.getName();
        String albumDescription = albumDto.getDescription();
        if(albumName != null) {
            album.setAlbumName(albumName);
            album.setDescription(albumDescription != null ? albumDescription : "");
        }
        else {
            throw new NameNotFoundException("Album name not found");
        }
        albumRepository.save(album);
    }

    public Map<Integer, String> getUserAlbumsNamesAndIds(User user){
        Map<Integer, String> albumNamesAndIds = new HashMap<>();
        Iterable<Album> allAlbums = albumRepository.findAllByUser(user);
        for (Album album : allAlbums) {
            albumNamesAndIds.put(album.getAlbumId(), album.getAlbumName());
        }
        return albumNamesAndIds;
    }

    public void deletePhotosFromAlbum(Album album){
        Iterable<Photo> photos = photoRepository.findByAlbumsContaining(album);
        for (Photo photo : photos) {
            photo.getAlbums().remove(album);
            if(photo.getGallery() != null){
                photoRepository.save(photo);
            }else {
                photoRepository.delete(photo);
            }
        }
    }

    public void deleteAlbumsByIds(List<Integer> albumIds) {
        for (Integer id : albumIds) {
            Album album = albumRepository.findById(id).orElseThrow(() -> new AlbumNotFoundException("Album with id " + id + " not found"));
            if(isAlbumOwner(id)) {
                deletePhotosFromAlbum(album);
                albumRepository.deleteById(id);
            }else{
                throw new UnauthorizedAlbumDeletionException("Unauthorized attempt to delete album with name: " + album.getAlbumName());
            }
        }
    }

    public boolean isAlbumOwner(int albumId) {
        User currentUser = (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Map<Integer, List<PhotoDto>> albums = getAllAlbumsForUser(currentUser);
        return albums.containsKey(albumId);
    }

}
