package com.irlix.irlixbook.service.user;

import com.irlix.irlixbook.config.security.utils.SecurityContextUtils;
import com.irlix.irlixbook.dao.entity.UserEntity;
import com.irlix.irlixbook.exception.MultipartException;
import com.irlix.irlixbook.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

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
    public byte[] getAvatar(String path) {
        return new byte[0];
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

    public List<String> getAllFiles(String dirname) {
        String path = StringUtils.hasLength(dirname)
                ? uploadPath + "/" + dirname
                : uploadPath;

        File rootDir = new File(path);
        log.info("ROOT DIR: {} {} {}", rootDir.exists(), rootDir.isDirectory(), rootDir.getAbsolutePath());

        List<String> result = new ArrayList<>();
        if (rootDir.exists() && rootDir.isDirectory()) {
            File[] files = rootDir.listFiles();
            result = Arrays.stream(files != null ? files : new File[0])
                    .map(File::getAbsolutePath)
                    .collect(Collectors.toList());
        }
        return result;
    }

    @Override
    public void deletePicture(String filepath) {
//        UserEntity user = SecurityContextUtils.getUserFromContext();
//        user.setAvatar(null);
        //todo add logic of delete file
//        userRepository.save(user);

        File file = new File(filepath);
        log.info("File for delete: {} {} {}", file.isFile(), file.exists(), file.getAbsolutePath());
        boolean delete = file.delete();
        log.info("DELETE FILE RESULT: {}", delete);
    }

    @Override
    public String update(MultipartFile file) {
        return null;
    }
}
