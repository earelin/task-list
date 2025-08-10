resource "google_compute_network" "task_list_network" {
  name                    = "task-list-network"
  auto_create_subnetworks = false
}

resource "google_compute_subnetwork" "task_list_subnetwork" {
  name                     = "task-list-subnetwork"
  ip_cidr_range            = "10.2.0.0/16"
  region                   = var.gcp_region
  network                  = google_compute_network.task_list_network.id
  private_ip_google_access = true
}

resource "google_compute_firewall" "allow_http" {
  name        = "allow-http"
  network     = google_compute_network.task_list_network.id
  description = "Creates firewall rule for HTTP connections"

  allow {
    protocol = "tcp"
    ports    = ["80"]
  }

  source_ranges = [google_compute_subnetwork.task_list_subnetwork.ip_cidr_range]
  target_tags   = ["http"]
}

resource "google_compute_firewall" "allow_ssh" {
  name        = "allow-ssh"
  network     = google_compute_network.task_list_network.id
  description = "Creates firewall rule for SSH connections"

  allow {
    protocol = "tcp"
    ports    = ["22"]
  }

  source_ranges = [google_compute_subnetwork.task_list_subnetwork.ip_cidr_range]
}

resource "google_compute_firewall" "allow_icmp" {
  name        = "allow-icmp"
  network     = google_compute_network.task_list_network.id
  description = "Creates firewall rule for ICMP connections"

  allow {
    protocol = "icmp"
  }

  source_ranges = [google_compute_subnetwork.task_list_subnetwork.ip_cidr_range]
}

resource "google_compute_firewall" "allow_rdp" {
  name        = "allow-rdp"
  network     = google_compute_network.task_list_network.id
  description = "Creates firewall rule for RDP connections"

  allow {
    protocol = "tcp"
    ports    = ["3389"]
  }

  source_ranges = [google_compute_subnetwork.task_list_subnetwork.ip_cidr_range]
}
