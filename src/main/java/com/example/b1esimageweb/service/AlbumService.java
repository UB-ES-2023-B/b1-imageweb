package com.example.b1esimageweb.service;

import com.example.b1esimageweb.Exceptions.AlbumNotFoundException;
import com.example.b1esimageweb.Exceptions.PhotoStorageException;
import com.example.b1esimageweb.Exceptions.UserNotFoundException;
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
            pho.setAlbum(album);
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

    public Map<Iterable<PhotoDto>,Integer> getAllAlbumsForUser(){
        List<PhotoDto> photos = new ArrayList<>();
        Map<Iterable<PhotoDto>,Integer> hMap = new HashMap<>();
        Set<Integer> albumIds = new HashSet<>();
        Object obj = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User currentUser = null;
        if(obj instanceof User){
            currentUser = (User) obj;
        }
        if(currentUser != null) {
            Iterable<Photo> allPhotos = photoRepository.findAll();
            Iterable<Album> allAlbums = albumRepository.findAll();
            for(Photo photo : allPhotos){
                if(photo.getAlbum() == null){
                    continue;
                }
                CloudBlob blob;
                try {
                    for (Album album : allAlbums){
                        if ((currentUser.getUserId() == album.getUser().getUserId()) && (album.getAlbumId() == photo.getAlbum().getAlbumId())){
                            blob = container.getBlockBlobReference(photo.getPhotoId().toString());
                            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                            blob.download(outputStream);
                            byte[] photoContent = outputStream.toByteArray();
                            photos.add(new PhotoDto(photoContent, photo.getPhotoId(), photo.getGallery(), photo.getPhotoName(), photo.getAlbum(), photo.getPhotoExtension(), photo.getPhotoDescription()));
                            albumIds.add(album.getAlbumId());
                        }
                    }
                } catch (URISyntaxException | StorageException e) {
                    e.printStackTrace();
                    return null;
                }
            }


        }
        hMap.put(photos,albumIds.size());
        return hMap;
    }

    public Iterable<PhotoDto> getPhotosByAlbum(Album album) {
        List<PhotoDto> photos = new ArrayList<>();

        for (Photo photo : photoRepository.findByAlbum(album)) {
            CloudBlob blob;
            try {
                blob = container.getBlockBlobReference(photo.getPhotoId().toString());
                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                blob.download(outputStream);
                byte[] photoContent = outputStream.toByteArray();
                photos.add(new PhotoDto(photoContent, photo.getPhotoId(), null, photo.getPhotoName(), photo.getAlbum(), photo.getPhotoExtension(), photo.getPhotoDescription()));
            } catch (URISyntaxException | StorageException e) {
                e.printStackTrace();
                return null;
            }
        }
        return photos;
    }

    public Album addPhotosToAlbum(int albumId,List<MultipartFile> photos){
        Album album = albumRepository.findById(albumId)
                .orElseThrow(() -> new AlbumNotFoundException("Album not found"));

        for (MultipartFile photoFile : photos) {
            try {
                Photo p = createPhoto(photoFile);
                p.setAlbum(album);
                photoRepository.save(p);
            } catch (Exception e) {
                throw new PhotoStorageException("Could not store photo");
            }
        }
        return album;
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
}
