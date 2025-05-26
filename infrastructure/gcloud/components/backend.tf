terraform {
  backend "gcs" {
    bucket  = "task-list-tf-state-bucket"
    prefix  = "terraform/state"
  }
}
