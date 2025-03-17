import { http } from "@/utils/http";

const urlHead = "/web_server/role";

export type RoleType = {
  roleId: string;
  roleName: string;
  roleCode: string;
  note: string;
  isDelete: boolean;
};

export const getRole = () => {
  return http.request("get", `${urlHead}`);
};

export const getRoleById = (id: string) => {
  return http.request("get", `${urlHead}/${id}`);
};

export const getPageByQuery = (params: {
  pageSize: number;
  pageNumber: number;
  roleName: string;
}) => {
  return http.request("get", `${urlHead}/page`, { params });
};

export const postRole = (data: RoleType) => {
  return http.request("post", `${urlHead}`, { data });
};

export const delRole = (roleId: string) => {
  return http.request("delete", `${urlHead}/${roleId}`);
};
