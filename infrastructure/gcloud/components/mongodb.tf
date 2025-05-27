resource "google_service_account" "mongodb_service_identity" {
  account_id   = "mongodb-service"
  display_name = "MongoDB Service Account"
}

resource "google_secret_manager_secret_iam_binding" "mongodb_service_mongodb_user_name_binding" {
  project   = var.gcp_project_id
  secret_id = module.mongodb_user_name.name
  role      = "roles/secretmanager.secretAccessor"
  members = [
    "serviceAccount:${google_service_account.mongodb_service_identity.email}"
  ]
}

resource "google_secret_manager_secret_iam_binding" "mongodb_service_mongodb_user_password_binding" {
  project   = var.gcp_project_id
  secret_id = module.mongodb_user_password.name
  role      = "roles/secretmanager.secretAccessor"
  members = [
    "serviceAccount:${google_service_account.mongodb_service_identity.email}"
  ]
}

resource "google_compute_instance" "mongodb_instance" {
  name         = "mongodb"
  machine_type = "e2-micro"
  zone         = "${var.gcp_region}-a"

  allow_stopping_for_update = true

  boot_disk {
    initialize_params {
      image = "debian-cloud/debian-12"
    }
  }

  metadata_startup_script = <<EOF
    sudo apt-get update
    sudo apt install gnupg curl -q -y
    curl -fsSL https://www.mongodb.org/static/pgp/server-8.0.asc | sudo gpg -o /usr/share/keyrings/mongodb-server-8.0.gpg --dearmor
    echo "deb [ signed-by=/usr/share/keyrings/mongodb-server-8.0.gpg ] http://repo.mongodb.org/apt/debian bookworm/mongodb-org/8.0 main" | sudo tee /etc/apt/sources.list.d/mongodb-org-8.0.list
    sudo apt-get update
    sudo apt-get install -y \
      mongodb-org=${var.mongodb_version} \
      mongodb-org-database=${var.mongodb_version} \
      mongodb-org-server=${var.mongodb_version} \
      mongodb-mongosh \
      mongodb-org-shell=${var.mongodb_version} \
      mongodb-org-mongos=${var.mongodb_version} \
      mongodb-org-tools=${var.mongodb_version} \
      mongodb-org-database-tools-extra=${var.mongodb_version}
    echo "Done"
  EOF

  network_interface {
    subnetwork = google_compute_subnetwork.task_list_subnetwork.name
  }

  scheduling {
    preemptible                 = true
    automatic_restart           = false
    provisioning_model          = "SPOT"
    instance_termination_action = "STOP"
  }

  service_account {
    email = google_service_account.mongodb_service_identity.email
    scopes = ["cloud-platform"]
  }
}
