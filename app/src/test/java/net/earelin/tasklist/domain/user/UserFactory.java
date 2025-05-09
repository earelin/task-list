package net.earelin.tasklist.domain.user;

public final class UserFactory {
  public static final String USER_ID = "sdf98jhd9a8f8kjdsaf";
  public static final String USER_EMAIL = "john.smith@example.com";
  public static final String USER_FIRST_NAME = "John";
  public static final String USER_SURNAME = "Smith";
  public static final String USER_PASSWORD = "encoded-secret";

  public static User createUser() {
    return createUserWithId(USER_ID);
  }

  public static User createUserWithId(String id) {
    return new User(id, USER_EMAIL, USER_FIRST_NAME, USER_SURNAME, USER_PASSWORD);
  }

  private UserFactory() {}
}
