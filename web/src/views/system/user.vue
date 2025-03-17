<template>
  <div class="main">
    <div class="w-[100%]">
      <el-card shadow="hover" class="bg-bg_color w-[99/100] pl-8 pt-[0px] pb-4">
        <div class="float-left">
          <el-form :inline="true" :model="queryData">
            <el-form-item label="用户昵称">
              <el-input
                v-model="queryData.username"
                class="w-[80%]"
                placeholder="请输入名称进行搜索"
              />
            </el-form-item>
            <el-form-item>
              <el-button
                :icon="Search"
                type="primary"
                @click="getAllPageByQuery()"
                >查询</el-button
              >
            </el-form-item>
          </el-form>
        </div>
        <div class="float-right">
          <el-button
            v-auth="'user:add'"
            type="primary"
            :icon="CirclePlus"
            @click="addUser()"
          >
            添加用户
          </el-button>
          <el-button :icon="Refresh" @click="getAllPageByQuery()">
            重置
          </el-button>
        </div>
      </el-card>
      <el-card shadow="hover" class="h-[80%] mt-1">
        <template #header>
          <IconifyIconOffline
            class="float-left"
            :icon="bustInSilhouette"
            width="20px"
            height="20px"
          />
          <span class="head-title pl-1"> 用户管理 </span>
        </template>
        <div>
          <el-table :height="tableHeight" :data="dataList">
            <el-table-column label="用户名" fixed width="200">
              <template #default="{ row }">
                <span>{{ row.userName }}</span>
              </template>
            </el-table-column>
            <el-table-column label="账号" width="200">
              <template #default="{ row }">
                <span>{{ row.userAccount }}</span>
              </template>
            </el-table-column>
            <el-table-column label="手机号" width="200">
              <template #default="{ row }">
                <span>{{ row.userPhone }}</span>
              </template>
            </el-table-column>
            <el-table-column label="邮箱" width="200">
              <template #default="{ row }">
                <span v-if="row.userEmail">{{ row.email }}</span>
                <span v-else>暂无</span>
              </template>
            </el-table-column>
            <el-table-column label="创建时间" width="200">
              <template #default="{ row }">
                <span>{{ dateFormat(row.userNewDate) }}</span>
              </template>
            </el-table-column>
            <el-table-column label="创建人员" width="200">
              <template #default="{ row }">
                <span>{{ row.userByUserName }}</span>
              </template>
            </el-table-column>
            <el-table-column label="更新时间" width="200">
              <template #default="{ row }">
                <span v-if="row.updateTime">{{
                  dateFormat(row.userSetDate)
                }}</span>
                <span v-else>暂无</span>
              </template>
            </el-table-column>
            <el-table-column
              v-auth="'user:set'"
              label="操作"
              fixed="right"
              width="300"
            >
              <template #default="{ row }">
                <el-button
                  v-auth="'user:set'"
                  type="primary"
                  :icon="Connection"
                  size="small"
                  @click="stRole(row)"
                >
                  分配角色
                </el-button>
                <el-button
                  v-auth="'user:set'"
                  type="warning"
                  :icon="EditPen"
                  size="small"
                  @click="update(row)"
                >
                  编辑
                </el-button>
                <el-button
                  v-auth="'user:del'"
                  type="danger"
                  :icon="Delete"
                  size="small"
                  @click="del(row)"
                >
                  删除
                </el-button>
              </template>
            </el-table-column>
          </el-table>
          <div class="pt-4">
            <el-pagination
              v-model:current-page="pageData.pageNumber"
              v-model:page-size="pageData.pageSize"
              :pager-count="5"
              background
              :page-sizes="[10, 50, 100, 200]"
              layout="total, sizes, prev, pager, next, jumper"
              :total="pageData.total"
              @size-change="getAllPageByQuery"
              @current-change="getAllPageByQuery"
            />
          </div>
        </div>
      </el-card>
    </div>
    <el-dialog
      v-model="UserDialog.show"
      :title="(UserDialog.usage.AorU ? '编辑' : '添加') + '用户'"
      width="40%"
    >
      <el-form
        ref="userForm"
        v-loading="UserDialog.usage.loading"
        :model="UserDialog.data"
        :rules="UserDialog.rules"
        :inline="true"
        label-width="80px"
        label-position="left"
      >
        <el-form-item label=" 用户昵称 " prop="userName" class="w-[43%]">
          <el-input v-model="UserDialog.data.userName" />
        </el-form-item>
        <el-form-item label=" 用户账号 " prop="userAccount" class="w-[42%]">
          <el-input v-model="UserDialog.data.userAccount" />
        </el-form-item>
        <el-form-item label=" 用户密码 " prop="userPassword" class="w-[43%]">
          <el-input
            v-model="UserDialog.data.userPassword"
            type="password"
            :disabled="UserDialog.usage.AorU"
          />
        </el-form-item>
        <el-form-item label=" 用户邮箱 " prop="userEmail" class="w-[42%]">
          <el-input v-model="UserDialog.data.userEmail" />
        </el-form-item>
        <el-form-item label=" 电话号码 " prop="userPhone" class="w-[43%]">
          <el-input v-model="UserDialog.data.userPhone" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button
          :disabled="UserDialog.usage.loading"
          @click="UserDialog.show = false"
        >
          取消
        </el-button>
        <el-button
          type="primary"
          :disabled="UserDialog.usage.loading"
          @click="userDiaLogTo(userForm)"
        >
          确认
        </el-button>
      </template>
    </el-dialog>
    <el-dialog v-model="RoleDialog.show" title="分配角色" width="20%">
      <span>选择角色: </span>
      <el-select
        v-model="RoleDialog.data"
        clearable
        filterable
        multiple
        placeholder="搜索或选择角色"
      >
        <el-option
          v-for="item in roleList"
          :key="item.roleId"
          :label="item.roleName"
          :value="item.roleId"
        />
      </el-select>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="RoleDialog.show = false">取消</el-button>
          <el-button type="primary" @click="toRoleUser()"> 确认 </el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, toRaw, onMounted, onBeforeUnmount } from "vue";
import {
  allPageByQuery,
  saveUser,
  updateUser,
  getUserById,
  delUserRole
} from "@/api/account/index";
import { saveUserRole } from "@/api/account/userRole";
import type { UserType } from "@/api/account/index";
import { getRole } from "@/api/role/index";
import type { RoleType } from "@/api/role/index";
import { message } from "@/utils/message";
import { dateFormat } from "@/utils/nlTool";
import { addDialog, closeDialog, closeAllDialog } from "@/components/ReDialog";
import { useNav } from "@/layout/hooks/useNav";
import { ElMessageBox, FormInstance } from "element-plus";
import { hasAuth } from "@/router/utils";
// 引入图标按需引入
import {
  Delete,
  Connection,
  EditPen,
  CirclePlus,
  Refresh,
  Search
} from "@element-plus/icons-vue";
import bustInSilhouette from "@iconify-icons/twemoji/bust-in-silhouette";

const { username } = useNav();

// 手机号校验
const validate_phoneNumber = (rule, value, callback) => {
  const reg = /^[1][3-9][0-9]{9}$/;
  if (value == "" || value == null) {
    callback();
  }
  if (!reg.test(value) && value != "") {
    callback(new Error("请输入正确的手机号码"));
  } else {
    callback();
  }
};
// 邮箱校验
const validate_email = (rule, value, callback) => {
  const emailRegExp = /^([a-zA-Z0-9_-])+@([a-zA-Z0-9_-])+(\.[a-zA-Z0-9_-])+/;
  const emailRegExp1 =
    /^([a-zA-Z]|[0-9])(\w|\-)+@[a-zA-Z0-9]+\.([a-zA-Z]{2,4})$/;
  if (value == "" || value == null) {
    callback();
  }
  if (
    (!emailRegExp.test(value) && value != "") ||
    (!emailRegExp1.test(value) && value != "")
  ) {
    callback(new Error("请输入有效邮箱格式！"));
  } else {
    callback();
  }
};

/** ref **/
const dataList = ref<Array<UserType>>([]);
const pageData = reactive({
  pageNumber: 1,
  pageSize: 10,
  total: 0
});
const queryData = reactive({
  username: ""
});
const UserDialog = reactive<iElDiaLog>({
  show: false,
  data: reactive<UserType>({
    userId: null,
    userAccount: null,
    userPassword: null,
    userName: null,
    userEmail: null,
    userPhone: null,
    userNewDate: null,
    userNewUserName: null,
    userSetDate: null,
    userSetUserName: null,
    userIsDelete: false,
    userHead: null
  }),
  usage: reactive({
    loading: false,
    AorU: false
  }),
  rules: {
    userAccount: [
      {
        required: true,
        message: "请填写用户账号",
        trigger: "blur"
      },
      {
        min: 3,
        max: 12,
        message: "账号长度在 3 到 12 位之间",
        trigger: "blur"
      }
    ],
    userPassword: [
      {
        required: true,
        message: "请填写密码",
        trigger: "blur"
      },
      { min: 3, message: "密码长度在 3 以上", trigger: "blur" }
    ],
    userName: [
      {
        required: true,
        message: "请填写用户名",
        trigger: "blur"
      },
      {
        min: 3,
        max: 12,
        message: "用户名长度在 3 到 12 位之间",
        trigger: "blur"
      }
    ],
    userEmail: [{ validator: validate_email, trigger: ["blur"] }],
    userPhone: [{ validator: validate_phoneNumber, trigger: ["blur"] }]
  }
});
const userForm = ref<FormInstance>();
const tableHeight = ref();
const roleList = ref<Array<RoleType>>([]);
const RoleDialog = reactive<iElDiaLog>({
  show: false,
  data: null,
  usage: {
    id: null
  }
});
const department = ref<any[]>([]);

onMounted(() => {
  getAllPageByQuery();
  getCardDetailsTableHeight();
});

/**public**/

// 动态计算表格高度
const getCardDetailsTableHeight = () => {
  const tableH = 370; //距离页面下方的高度
  const tableHeightDetil = window.innerHeight - tableH;
  if (tableHeightDetil <= 100) {
    tableHeight.value = 100;
  } else {
    tableHeight.value = window.innerHeight - tableH;
  }
};
// 监听窗口变化动态设置表格高度
window.onresize = () => {
  getCardDetailsTableHeight();
};

const del = (row: UserType) => {
  ElMessageBox.confirm("确认要删除此用户吗？", "提示", {
    confirmButtonText: "确认",
    cancelButtonText: "取消",
    type: "warning"
  })
    .then(() => {
      delUserRole(row.userId)
        .then((res: R<any>) => {
          if (res.code == 200) {
            message("删除用户成功", { type: "success" });
            getAllPageByQuery();
          } else {
            message("删除用户失败", { type: "success" });
          }
        })
        .catch(error => {
          console.error("删除用户异常", error);
        });
    })
    .catch(() => {
      message("取消删除用户");
    });
};

const stRole = (row: UserType) => {
  getRole().then((res: R<Array<RoleType>>) => {
    if (res.code == 200) {
      roleList.value = res.data;
      RoleDialog.usage.id = row.userId;
      getUserById(row.userId).then((byUserRes: R<UserType[]>) => {
        if (byUserRes.code == 200) {
          RoleDialog.data = (byUserRes.data.roles || []).map(
            (role: RoleType) => {
              return role.roleId;
            }
          );
          RoleDialog.show = true;
        } else {
          message("获取该用户角色信息失败", { type: "error" });
        }
      });
    } else {
      message("获取角色信息失败", { type: "error" });
    }
  });
};

const toRoleUser = () => {
  console.log(RoleDialog);
  saveUserRole({ userId: RoleDialog.usage.id, roleIdStr: RoleDialog.data })
    .then((res: R<any>) => {
      if (res.code == 200) {
        message("分配角色成功", { type: "success" });
        RoleDialog.show = false;
      } else {
        message(res.msg, { type: "error" });
      }
    })
    .catch(error => {
      console.error("分配角色异常", error);
    });
};

const update = (row: UserType) => {
  UserDialog.usage.loading = true;
  getById(
    row.userId,
    res => {
      UserDialog.usage.AorU = true;
      UserDialog.data = res;
      UserDialog.show = true;
    },
    () => {
      message("获取用户信息失败", { type: "error" });
    }
  );

  UserDialog.usage.loading = false;
};

const addUser = () => {
  UserDialog.usage.loading = true;
  UserDialog.usage.AorU = false;
  UserDialog.data = {
    userId: null,
    userAccount: null,
    userPassword: null,
    userName: null,
    userEmail: null,
    userPhone: null,
    userNewDate: null,
    userNewUserName: null,
    userSetDate: null,
    userSetUserName: null,
    userIsDelete: 0,
    userHead: null
  };
  UserDialog.show = true;
  UserDialog.usage.loading = false;
};

const userDiaLogTo = async (formEl: FormInstance | undefined) => {
  if (!formEl) return;
  await formEl.validate((valid, fields) => {
    if (valid) {
      UserDialog.usage.loading = true;
      console.log(username);
      if (UserDialog.usage.AorU) {
        UserDialog.data.userSetUserName = username;
        updateUser(UserDialog.data)
          .then((res: R<any>) => {
            if (res.code == 200) {
              getAllPageByQuery();
              UserDialog.show = false;
              message("修改成功", { type: "success" });
            } else {
              message(res.msg, { type: "error" });
            }
          })
          .catch(error => {
            console.error(error.message);
          })
          .finally(() => {
            UserDialog.usage.loading = false;
          });
      } else {
        UserDialog.data.userNewUserName = username;
        saveUser(UserDialog.data)
          .then((res: R<any>) => {
            if (res.code == 200) {
              message("操作成功", { type: "success" });
              getAllPageByQuery();
              UserDialog.show = false;
            } else {
              message(res.msg, { type: "error" });
            }
          })
          .catch(error => {
            console.error(error.message);
          })
          .finally(() => {
            UserDialog.usage.loading = false;
          });
      }
    } else {
      console.log("无法通过验证", fields);
    }
  });
};
/** http **/
/**
 *! 查询所有角色
 */
const getAllPageByQuery = () => {
  allPageByQuery({
    pageNumber: pageData.pageNumber,
    pageSize: pageData.pageSize,
    userName: queryData.username
  })
    .then((res: R<any>) => {
      if (res.code == 200) {
        dataList.value = res.data.records;
        pageData.total = res.data.totalRow;
      } else {
        message("获取用户信息失败", {
          type: "error"
        });
      }
    })
    .catch(error => {
      console.error(error.message);
    });
};

/**
 * ! 更具用户id获取角色
 */
const getById = (
  id: string,
  success: (res: UserType) => void,
  error: (res: any) => void,
  fin?: () => void
) => {
  getUserById(id)
    .then((res: R<UserType>) => {
      if (res.code == 200) {
        success(res.data);
      } else {
        error(null);
      }
    })
    .catch(error => {
      console.log("获取角色异常", error);
      error(null);
    })
    .finally(() => {
      if (fin) {
        fin();
      }
    });
};
</script>

<style lang="scss" scoped></style>
