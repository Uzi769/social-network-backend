package com.irlix.irlixbook.service.user.avatar;

import org.springframework.web.multipart.MultipartFile;

public interface AvatarService {

    String getAvatar(String path);

    String uploading(MultipartFile file);

    void delete();

    String update(MultipartFile file);
}
