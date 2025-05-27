resource "google_compute_network" "task_list_network" {
  name                    = "task-list-network"
  auto_create_subnetworks = false
}

resource "google_compute_subnetwork" "task_list_subnetwork" {
  name          = "task-list-subnetwork"
  region        = var.gcp_region
  network       = google_compute_network.task_list_network.id
}
