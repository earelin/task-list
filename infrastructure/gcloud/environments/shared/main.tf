module "github_actions" {
  source             = "../../components/github_actions"
  gcp_project_id     = local.gcp_project_id
}
