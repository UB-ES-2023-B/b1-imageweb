package com.example.b1esimageweb.service;

import com.example.b1esimageweb.Exceptions.AlbumNotFoundException;
import com.example.b1esimageweb.Exceptions.PhotoStorageException;
import com.example.b1esimageweb.Exceptions.UserNotFoundException;
import com.example.b1esimageweb.model.Album;
import com.example.b1esimageweb.model.Photo;
import com.example.b1esimageweb.model.User;
import com.example.b1esimageweb.repository.AlbumRepository;
import com.example.b1esimageweb.repository.PhotoRepository;
import com.example.b1esimageweb.web.dto.AlbumDto;
import com.microsoft.azure.storage.CloudStorageAccount;
import com.microsoft.azure.storage.StorageException;
import com.microsoft.azure.storage.blob.CloudBlob;
import com.microsoft.azure.storage.blob.CloudBlobClient;
import com.microsoft.azure.storage.blob.CloudBlobContainer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.naming.NameNotFoundException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.security.InvalidKeyException;
import java.util.List;

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
            Photo coverPhoto = createPhoto(albumDto.getCoverPhoto());
            coverPhoto.setAlbum(album);
            album.setCoverPhoto(coverPhoto);
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

    public Iterable<Album> getAllAlbumsForUser(){
        return albumRepository.findAll();
    }

    public Album addPhotosToAlbum(int albumId,List<MultipartFile> photos){
        Album album = albumRepository.findById(albumId)
                .orElseThrow(() -> new AlbumNotFoundException("Album not found"));

        for (MultipartFile photoFile : photos) {
            try {
                Photo photo = createPhoto(photoFile);
                photo.setAlbum(album);
                photoRepository.save(photo);
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

        CloudBlob blob;
        try {
            blob = container.getBlockBlobReference(fileName);
            byte[] decodedBytes = photo.getBytes();
            blob.uploadFromByteArray(decodedBytes, 0, decodedBytes.length);
        } catch (URISyntaxException | StorageException e) {
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        photoRepository.save(newPhoto);
        return newPhoto;
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
