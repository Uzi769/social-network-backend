package com.irlix.irlixbook.service.user;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface AvatarService {

    byte[] getAvatar(String path);

    String uploading(MultipartFile file);

    void deletePicture();

    List<String> getAllFiles(String dirname);

    String update(MultipartFile file);
}
