import settings2Line from "@iconify-icons/ri/settings-2-line";
import bustInSilhouette from "@iconify-icons/twemoji/bust-in-silhouette";
import bustsInSilhouette from "@iconify-icons/twemoji/busts-in-silhouette";
import chessPawn from "@iconify-icons/twemoji/chess-pawn";

export default {
  path: "/system",
  redirect: "/system/user",
  meta: {
    icon: settings2Line,
    title: "系统管理"
  },
  children: [
    {
      path: "/system/user",
      name: "user",
      component: () => import("@/views/system/user.vue"),
      meta: {
        icon: bustInSilhouette,
        title: "用户管理",
        roles: ["user"],
        auths: ["user:add", "user:set", "user:del"],
        transition: {
          // 页面进场动画
          enterTransition: "animate__fadeInDown",
          // 页面离场动画
          leaveTransition: "animate__fadeOutDownBig"
        }
      }
    },
    {
      path: "/system/role",
      name: "role",
      component: () => import("@/views/system/role.vue"),
      meta: {
        icon: bustsInSilhouette,
        title: "角色管理",
        roles: ["role"],
        auths: ["role:add", "role:set", "role:del"],
        transition: {
          // 页面进场动画
          enterTransition: "animate__fadeInDown",
          // 页面离场动画
          leaveTransition: "animate__fadeOutDownBig"
        }
      }
    },
    {
      path: "/system/power",
      name: "power",
      component: () => import("@/views/system/power.vue"),
      meta: {
        title: "权限管理",
        roles: ["admin"],
        icon: chessPawn,
        transition: {
          // 页面进场动画
          enterTransition: "animate__fadeInDown",
          // 页面离场动画
          leaveTransition: "animate__fadeOutDownBig"
        }
      }
    }
  ]
};
