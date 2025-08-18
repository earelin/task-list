resource "google_artifact_registry_repository" "task_list_repository" {
  location      = local.gcp_region
  repository_id = "task-list"
  description   = "Task List Application Docker Repository"
  format        = "DOCKER"

  docker_config {
    immutable_tags = true
  }
}

module "github_actions" {
  source             = "../../components/github_actions"
  gcp_project_id     = local.gcp_project_id
  gcp_region         = local.gcp_region
  task_list_repository_name = google_artifact_registry_repository.task_list_repository.name
}

module "network" {
  source     = "../../components/network"
  gcp_region = local.gcp_region
}
