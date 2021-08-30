package com.irlix.irlixbook.service.user.avatar;

import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

public interface AvatarService {

    String getAvatar();

    String getAvatarByUserID(UUID id);

    String uploading(MultipartFile file);

    void delete();

    String update(MultipartFile file);
}
