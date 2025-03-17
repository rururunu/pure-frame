import { http } from "@/utils/http";

const urlHead = `/web_server/role_power`;

export interface RolePower {
  id: number;
  roleId: string;
  powerId: string;
}

export const postRoleAuthority = (roleId: string, powerStr: string) => {
  return http.request(
    "post",
    `${urlHead}?roleId=${roleId}&powerStr=${powerStr}`
  );
};
