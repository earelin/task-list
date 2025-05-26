resource "google_secret_manager_secret" "mongodb-user" {
  secret_id = "mongodb-user"

  labels = {
    label = "MongoDB User"
  }

  replication {
    user_managed {
      replicas {
        location = var.gcp_region
      }
    }
  }
}

resource "google_secret_manager_secret_version" "mongodb-user-initial" {
  secret = google_secret_manager_secret.mongodb-user.id

  secret_data = "secret-data"
}
