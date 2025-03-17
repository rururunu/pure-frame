import { http } from "@/utils/http";

const urlHead = `/web_server/user_role`;

export interface UserRole {
  id: number;
  userId: string;
  roleId: string;
}

export const saveUserRole = (param: { userId: string; roleIdStr: string }) => {
  return http.request(
    "put",
    `${urlHead}?userId=${param.userId}&roleIdStr=${param.roleIdStr}`
  );
};
