package vn.aptech.pixelpioneercourse.service;

import vn.aptech.pixelpioneercourse.entities.Enrollment;
import vn.aptech.pixelpioneercourse.entities.User;

public interface EmailService {
    void sendDeletionEmail(Enrollment enrollment, String reason);
}
