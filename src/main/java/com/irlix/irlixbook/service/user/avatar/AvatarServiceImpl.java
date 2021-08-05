package com.irlix.irlixbook.service.user.avatar;

import com.irlix.irlixbook.config.security.utils.SecurityContextUtils;
import com.irlix.irlixbook.dao.entity.UserEntity;
import com.irlix.irlixbook.exception.MultipartException;
import com.irlix.irlixbook.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.UUID;

import static com.irlix.irlixbook.utils.Consts.FILE_SIZE_EXCEEDED;
import static com.irlix.irlixbook.utils.Consts.PHOTO_UPLOADED;

@Service
@Slf4j
@RequiredArgsConstructor
public class AvatarServiceImpl implements AvatarService {

    private final UserRepository userRepository;

    @Value("${photo.upload.path}")
    private String uploadPath;

    @Value("${photo.upload.root}")
    private String uploadRoot;

    @Override
    public String getAvatar(String path) {
        UserEntity user = SecurityContextUtils.getUserFromContext();
        return user.getAvatar();
    }

    @Override
    public String uploading(MultipartFile file) {
        if (file == null) {
            log.info("File of avatar is null");
            throw new IllegalArgumentException("File of avatar is null");
        }
        if (file.getSize() > 10485760) {
            log.info(FILE_SIZE_EXCEEDED);
            throw new MultipartException(FILE_SIZE_EXCEEDED);
        }
        UserEntity user = SecurityContextUtils.getUserFromContext();

        String folderName = user.getEmail().substring(0, user.getEmail().indexOf("@"));
        String folderPath = uploadPath + "/" + folderName;

        File uploadDir = new File(folderPath);
        if (!uploadDir.exists()) {
            boolean mkdir = uploadDir.mkdir();
            log.info("create dir: {}", mkdir);
        }
        String originFileName = file.getOriginalFilename();
        String fileName = UUID.randomUUID() + originFileName.substring(originFileName.lastIndexOf("."));

        try {
            Files.write(Path.of(folderPath + "/" + fileName), file.getBytes());
        } catch (IOException e) {
            log.error("Save avatar error: {}", e.getMessage());
        }

        String photoName = uploadRoot + folderName + "/" + fileName;
        user.setAvatar(photoName);
        userRepository.save(user);
        log.info(PHOTO_UPLOADED);
        return photoName;
    }

    @Override
    public void delete() {
        UserEntity user = SecurityContextUtils.getUserFromContext();
        String avatar = user.getAvatar();

        String filepath = avatar.replace(uploadRoot, uploadPath + "/");
        this.deleteFile(filepath);
        user.setAvatar(null);
        userRepository.save(user);
    }

    @Override
    public String update(MultipartFile file) {
        this.delete();
        return this.uploading(file);
    }

    private void deleteFile(String filepath) {
        File file = new File(filepath);
        log.info("File for delete: {} {} {}", file.isFile(), file.exists(), file.getAbsolutePath());
        boolean delete = file.delete();
        log.info("DELETE FILE RESULT: {}", delete);
    }
}
