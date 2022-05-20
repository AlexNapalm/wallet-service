package ru.krasnopolsky.exception;

public class BusinessException extends RuntimeException {

    public enum ErrorCode {

        BALANCE_LESS_THAN_ZERO,
        BALANCE_CHANGE_EXCEEDS_MAX_LIMIT,
        PLAYER_BLACKLISTED;

        public String formatErrorCode() {
            return "error." + name().toLowerCase().replace("_", "-");
        }
    }

    public BusinessException(ErrorCode error) {
        super(error.formatErrorCode(), null, true, false);
    }
}
