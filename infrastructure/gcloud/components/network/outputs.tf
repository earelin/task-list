output "network_name" {
  value = google_compute_network.task_list_network.name
}

output "subnetwork_name" {
  value = google_compute_subnetwork.task_list_subnetwork.name
}
