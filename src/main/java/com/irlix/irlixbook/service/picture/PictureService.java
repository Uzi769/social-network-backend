package com.irlix.irlixbook.service.picture;

import com.irlix.irlixbook.dao.entity.Content;
import com.irlix.irlixbook.dao.entity.Picture;
import com.irlix.irlixbook.dao.model.picture.PictureOutput;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

public interface PictureService {

    PictureOutput uploading(MultipartFile file);

    void deletePicture(UUID id);

    List<Picture> addContentToPicture(List<UUID> pictureIdList, Content content);

    List<PictureOutput>  getList();

}
