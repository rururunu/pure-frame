import { http } from "@/utils/http";

const urlHead = "/web_server/user";

export type UserType = {
  userId: string;
  userAccount: string;
  userPassword: string;
  userName: string;
  userEmail: string;
  userPhone: string;
  userNewDate: string;
  userNewUserName: string;
  userSetDate: string;
  userSetUserName: string;
  userIsDelete: boolean;
  userHead: number;
};

export const allPageByQuery = (params: {
  pageSize: number;
  pageNumber: number;
  userName?: string;
  phone?: string;
}) => {
  return http.request("get", `${urlHead}/pageByQuery`, { params });
};

export const saveUser = (data: UserType) => {
  return http.request("put", `${urlHead}`, { data });
};

export const updateUser = (data: UserType) => {
  return http.request("post", `${urlHead}`, { data });
};

export const getUserById = (id: string) => {
  return http.request("get", `${urlHead}/byId/${id}`);
};

export const delUserRole = (id: string) => {
  return http.request("delete", `${urlHead}/${id}`);
};
