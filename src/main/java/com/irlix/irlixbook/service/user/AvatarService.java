package com.irlix.irlixbook.service.user;

import org.springframework.web.multipart.MultipartFile;

public interface AvatarService {

    byte[] getAvatar(String path);

    String uploading(MultipartFile file);

    void deletePicture();

    String update(MultipartFile file);
}
