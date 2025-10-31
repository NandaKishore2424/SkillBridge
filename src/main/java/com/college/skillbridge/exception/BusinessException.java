package com.college.skillbridge.exception;

public class BusinessException extends RuntimeException {
    private final String errorCode;
    
    public BusinessException(String message, String errorCode) {
        super(message);
        this.errorCode = errorCode;
    }
    
    public String getErrorCode() {
        return errorCode;
    }
}

class BatchCapacityExceededException extends BusinessException {
    public BatchCapacityExceededException(String message) {
        super(message, "BATCH_001");
    }
}

class TrainerNotAvailableException extends BusinessException {
    public TrainerNotAvailableException(String message) {
        super(message, "TRAINER_001");
    }
}

class AssessmentDeadlineException extends BusinessException {
    public AssessmentDeadlineException(String message) {
        super(message, "ASSESSMENT_001");
    }
}

class InvalidLearningPathException extends BusinessException {
    public InvalidLearningPathException(String message) {
        super(message, "PATH_001");
    }
}

class UserAlreadyEnrolledException extends BusinessException {
    public UserAlreadyEnrolledException(String message) {
        super(message, "ENROLLMENT_001");
    }
}