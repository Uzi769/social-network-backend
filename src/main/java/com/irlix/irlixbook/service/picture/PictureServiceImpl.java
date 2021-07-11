package com.irlix.irlixbook.service.picture;

import com.irlix.irlixbook.dao.entity.Content;
import com.irlix.irlixbook.dao.entity.Picture;
import com.irlix.irlixbook.dao.model.picture.PictureOutput;
import com.irlix.irlixbook.exception.ConflictException;
import com.irlix.irlixbook.exception.MultipartException;
import com.irlix.irlixbook.repository.PictureRepository;
import com.irlix.irlixbook.utils.Consts;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static com.irlix.irlixbook.utils.Consts.FILE_SIZE_EXCEEDED;

@Slf4j
@Service
@RequiredArgsConstructor
public class PictureServiceImpl implements PictureService{

    private final PictureRepository pictureRepository;
    private final ConversionService conversionService;

    @Value("${picture.upload.path}")
    private String uploadPath;

    @Value("${picture.upload.root}")
    private String uploadRoot;

    @Override
    public PictureOutput uploading(MultipartFile file) {
        if (file.getSize() > 10485760) {
            log.info(FILE_SIZE_EXCEEDED);
            throw new MultipartException(FILE_SIZE_EXCEEDED);
        }
        Picture picture = new Picture();
        if (file != null) {
            File uploadDir = new File(uploadPath);
            if (!uploadDir.exists()) {
                uploadDir.mkdir();
            }
            String originFileName = file.getOriginalFilename();
            UUID id = UUID.randomUUID();
            String fileName = id + originFileName.substring(originFileName.lastIndexOf("."));

            try {
                file.transferTo(new File(uploadPath + fileName));
            } catch (IOException e) {
                e.printStackTrace();
            }

            picture = Picture.builder()
                    .id(id)
                    .url(uploadRoot + fileName)
                    .build();

            pictureRepository.save(picture);
            log.info(Consts.PICTURE_UPLOADED);
        }
        return conversionService.convert(picture, PictureOutput.class);
    }

    @Override
    public void deletePicture(UUID id) {
        Picture picture = pictureRepository.findById(id).orElseThrow(() -> {
            log.error(Consts.PICTURE_NOT_FOUND);
            return new ConflictException(Consts.PICTURE_NOT_FOUND);
        });
        pictureRepository.delete(picture);
        log.info(Consts.PICTURE_DELETED);
    }

    @Override
    public List<Picture> addContentToPicture(List<UUID> pictureIdList, Content content) {
        List<Picture> pictures = new ArrayList<>();
        pictureIdList.stream()
                .map(id -> pictureRepository.findById(id).orElseThrow(()-> {
                    log.error(Consts.PICTURE_NOT_FOUND);
                    return new ConflictException(Consts.PICTURE_NOT_FOUND);
                }))
                .forEach(picture -> {
                    picture.setContent(content);
                    Picture savedPicture = pictureRepository.save(picture);
                    pictures.add(savedPicture);
                });
        return pictures;
    }
}