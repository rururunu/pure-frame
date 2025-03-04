import { http } from "@/utils/http";

const urlHead = "web_server/user";

export interface User {
  userName: string;
  userAccount: string;
  userPassword: string;

  userId?: string;
  userNewData?: any;
  userSetDate?: any;
  userNewUserName?: string;
  userHead?: string;
  userPhone?: string;
  userEmail?: string;
  roles?: any[];
  powerCodes?: string[];
}

export const getUser = () => {
  return http.request("get", `${urlHead}/all`);
};
