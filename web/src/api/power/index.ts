import { http } from "@/utils/http";

const urlHead = `/web_server/power`;

export interface Power {
  powerId: string;
  powerSupId: string;
  powerCode: string;
  powerName: string;
  isDelete: boolean;
}

export const getElTree = () => {
  return http.request("get", `${urlHead}/elTree`);
};

export const getTreeAll = () => {
  return http.request("get", `${urlHead}/tree`);
};

export const postAuthority = (data: Power) => {
  return http.request("post", `${urlHead}`, { data });
};

export const postAuthorityPack = (
  data: Power,
  ok: (res: any) => void,
  err: (res: any) => void,
  cch?: (error: any) => void,
  fiy?: () => void
) => {
  return http
    .request("post", `${urlHead}`, { data })
    .then((res: any) => {
      if (res.code == 200) {
        ok(res);
      } else {
        err(res);
      }
    })
    .catch(error => {
      console.error(error);
      if (cch) {
        cch(error);
      }
    })
    .finally(() => {
      if (fiy) {
        fiy();
      }
    });
};

export const getById = (id: any) => {
  return http.request("get", `${urlHead}/${id}`);
};

export const getByIdPack = (
  id: any,
  ok: (res: any) => void,
  err: (res: any) => void,
  cch?: (error: any) => void,
  fiy?: () => void
) => {
  return http
    .request("get", `${urlHead}/${id}`)
    .then((res: any) => {
      if (res.code == 200) {
        ok(res);
      } else {
        err(res);
      }
    })
    .catch(error => {
      console.error(error);
      if (cch) {
        cch(error);
      }
    })
    .finally(() => {
      if (fiy) {
        fiy();
      }
    });
};

export const delPowerById = (powerId: string) => {
  return http.request("delete", `${urlHead}/${powerId}`);
};
