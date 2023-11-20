package com.example.b1esimageweb.service;

import com.example.b1esimageweb.Exceptions.UserNotFoundException;
import com.example.b1esimageweb.model.Album;
import com.example.b1esimageweb.model.User;
import com.example.b1esimageweb.repository.AlbumRepository;
import com.example.b1esimageweb.web.dto.AlbumDto;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.naming.NameNotFoundException;

@Service
public class AlbumService {

    private AlbumRepository albumRepository;

    public AlbumService(AlbumRepository albumRepository) {
        this.albumRepository = albumRepository;
    }

    public Album createAlbum(AlbumDto albumDto) throws NameNotFoundException {
        Album album = new Album();
        String albumName = albumDto.getName();
        String albumDescription = albumDto.getDescription();
        if(albumName != null) {
            album.setAlbumName(albumName);
            album.setDescription(albumDescription != null ? albumDescription : "");
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
}
