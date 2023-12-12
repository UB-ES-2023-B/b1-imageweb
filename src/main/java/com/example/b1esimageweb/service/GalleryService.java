package com.example.b1esimageweb.service;

import java.io.IOException;
import java.net.URISyntaxException;
import java.security.InvalidKeyException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.example.b1esimageweb.model.User;
import com.microsoft.azure.storage.blob.CloudBlockBlob;
import org.apache.tomcat.util.http.fileupload.ByteArrayOutputStream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.multipart.MultipartFile;

import com.example.b1esimageweb.Exceptions.GalleryNotFoundException;
import com.example.b1esimageweb.Exceptions.PhotoNotFoundException;
import com.example.b1esimageweb.model.Gallery;
import com.example.b1esimageweb.model.Photo;
import com.example.b1esimageweb.repository.GalleryRepository;
import com.example.b1esimageweb.repository.PhotoRepository;
import com.example.b1esimageweb.web.dto.PhotoDto;
import com.microsoft.azure.storage.CloudStorageAccount;
import com.microsoft.azure.storage.StorageException;
import com.microsoft.azure.storage.blob.CloudBlob;
import com.microsoft.azure.storage.blob.CloudBlobClient;
import com.microsoft.azure.storage.blob.CloudBlobContainer;

@org.springframework.stereotype.Service
public class GalleryService {


    private PhotoRepository photoRepository;
    private GalleryRepository galleryRepository;
    private CloudStorageAccount account;
    private CloudBlobClient serviceClient;
    private CloudBlobContainer container;

    @Autowired
    public GalleryService(PhotoRepository photoRepository, GalleryRepository galleryRepository, @Value("${azure.storage.conection.string}") String storageConnectionAzure, @Value("${azure.storage.container.name}") String nameContainer) {
        this.photoRepository = photoRepository;
        this.galleryRepository = galleryRepository;

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

    public PhotoDto addNewPhoto(Integer galleryId, MultipartFile photo) {
        Gallery gallery = galleryRepository.findById(galleryId).orElseThrow(() -> new GalleryNotFoundException("Gallery with id " + galleryId + " not found"));

        Photo newPhoto = new Photo();

        String fileName = photo.getOriginalFilename();
        int lastDotIndex = fileName.lastIndexOf(".");
        String extension = fileName.substring(lastDotIndex + 1);
        newPhoto.setPhotoName(fileName);
        newPhoto.setPhotoExtension(extension);
        newPhoto.setGallery(gallery);
        photoRepository.save(newPhoto);

        CloudBlob blob;
        try {
            blob = container.getBlockBlobReference(newPhoto.getPhotoId().toString());
            byte[] decodedBytes = photo.getBytes();
            blob.uploadFromByteArray(decodedBytes, 0, decodedBytes.length);
            PhotoDto newPhotoDto = new PhotoDto(decodedBytes, newPhoto.getPhotoId(), gallery, fileName, null, extension, "");
            return newPhotoDto;
        } catch (URISyntaxException | StorageException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Iterable<PhotoDto> getAllPhotos() {
        List<PhotoDto> photos = new ArrayList<>();
        for (Photo photo : photoRepository.findAll()) {
            CloudBlob blob;
            try {
                blob = container.getBlockBlobReference(photo.getPhotoId().toString());
                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                blob.download(outputStream);
                byte[] photoContent = outputStream.toByteArray();
                photos.add(new PhotoDto(photoContent, photo.getPhotoId(), photo.getGallery(), photo.getPhotoName(), photo.getAlbums(), photo.getPhotoExtension(), photo.getPhotoDescription()));
            } catch (URISyntaxException | StorageException e) {
                e.printStackTrace();
                return null;
            }
        }
        return photos;
    }

     public Iterable<PhotoDto> getRandomNumberOfPhotos(int number) {
        List<PhotoDto> photos = new ArrayList<>();
        for (Photo photo : photoRepository.findAll()) {
            if(photos.size()==number){
                break;
            }
            if(photo.getGallery()!=null){
                CloudBlob blob;
                try {
                    blob = container.getBlockBlobReference(photo.getPhotoId().toString());
                    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                    blob.download(outputStream);
                    byte[] photoContent = outputStream.toByteArray();
                    photos.add(new PhotoDto(photoContent, photo.getPhotoId(), photo.getGallery(), photo.getPhotoName(), photo.getAlbums(), photo.getPhotoExtension(), photo.getPhotoDescription()));
                } catch (URISyntaxException | StorageException e) {
                    e.printStackTrace();
                    return null;
                }
            }
            
        }
        return photos;
    }

    public PhotoDto getPhotoById(int photoId) {
        Photo photo = photoRepository.findById(photoId).orElseThrow(() -> new PhotoNotFoundException("Photo with id " + photoId + "not found"));
        CloudBlob blob;
        try {
            blob = container.getBlockBlobReference(photo.getPhotoId().toString());
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            blob.download(outputStream);
            byte[] photoContent = outputStream.toByteArray();
            return new PhotoDto(photoContent, photo.getPhotoId(), photo.getGallery(), photo.getPhotoName(), photo.getAlbums(), photo.getPhotoExtension(), photo.getPhotoDescription());
        } catch (URISyntaxException | StorageException e) {
            e.printStackTrace();

        }
        return null;
    }

    public Gallery getGalleryById(int galleryId) {
        return galleryRepository.findById(galleryId).orElseThrow(() -> new GalleryNotFoundException("Gallery with id " + galleryId + " not found"));
    }

    public Iterable<PhotoDto> getPhotosByGallery(Gallery gallery) {
        List<PhotoDto> photos = new ArrayList<>();

        for (Photo photo : photoRepository.findByGallery(gallery)) {
            CloudBlob blob;
            try {
                blob = container.getBlockBlobReference(photo.getPhotoId().toString());
                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                blob.download(outputStream);
                byte[] photoContent = outputStream.toByteArray();
                photos.add(new PhotoDto(photoContent, photo.getPhotoId(), photo.getGallery(), photo.getPhotoName(), photo.getAlbums(), photo.getPhotoExtension(), photo.getPhotoDescription()));
            } catch (URISyntaxException | StorageException e) {
                e.printStackTrace();
                return null;
            }
        }
        return photos;
    }

    public String deleteGalleryPhotos(List<Integer> photosId) {
        for (int photoId : photosId) {
            if (photoRepository.findById(photoId).get() != null) {
                Photo photo = photoRepository.findById(photoId).orElseThrow(() -> new PhotoNotFoundException("Photo with id " + photoId + "not found"));
                photoRepository.deleteById(photoId);
                CloudBlockBlob blockBlob;
                try {
                    blockBlob = container.getBlockBlobReference(photo.getPhotoId().toString());
                    blockBlob.deleteIfExists();
                } catch (URISyntaxException | StorageException e) {

                    e.printStackTrace();
                    return "An error ocurred while deleting photo from gallery";
                }

            }
        }
        return "Photos successfully deleted from gallery";
    }

    public Photo updateInfoPhotoById(int photoId, String photoName, String photoDescription) {
        Photo photo = photoRepository.findById(photoId).orElseThrow(() -> new PhotoNotFoundException("Photo with id " + photoId + "not found"));
        if (photo != null) {
            photo.setPhotoName(photoName);
            photo.setPhotoDescription(photoDescription);
            return photoRepository.save(photo);
        }
        return null;
    }

    public boolean isGalleryOwner(int galleryId) {
        User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return currentUser.getGallery().getGalleryrId() == galleryId;

    }
     public boolean isPhotoOwner(int photoId){
         User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
         return getPhotoById(photoId).getGallery().getGalleryrId() == currentUser.getGallery().getGalleryrId();
     }
}