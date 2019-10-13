package com.amrdeveloper.fastmind.objects;

public class RegisterResponse {
    private String status;
    private State state;
    private Throwable throwable;

    public static RegisterResponse success(String status) {
        return new RegisterResponse(status, State.SUCCESS, null);
    }

    public static RegisterResponse error(Throwable throwable) {
        return new RegisterResponse("", State.ERROR, throwable);
    }

    public static RegisterResponse progress(String status) {
        return new RegisterResponse(status, State.PROGRESS, null);

    }

    private RegisterResponse(String status, State state, Throwable throwable) {
        this.status = status;
        this.state = state;
        this.throwable = throwable;
    }

    public String getStatus() {
        return status;
    }

    public State getState() {
        return state;
    }

    public Throwable getThrowable() {
        return throwable;
    }
}
