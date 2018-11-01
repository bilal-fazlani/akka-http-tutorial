package example.part_5_authentication.V_OIDC_Authorization

object Authorization {
  def hasPermission(accessToken: AccessToken, permission: String): Boolean = {
    accessToken.permissions.contains(permission)
  }
}
