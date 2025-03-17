<template>
  <div class="main">
    <el-card shadow="hover" class="w-[100%] full">
      <div class="flex flex-row justify-start w-[75%] float-left">
        <IconifyIconOffline :icon="chessPawn" width="25px" height="25px" />
        <span class="head-title pl-2 pr-2">权限管理</span>
      </div>
      <div class="pl-2 pr-2 float-right w-[20%] flex flex-row justify-end">
        <el-button type="success" :icon="CirclePlus" @click="add()"
          >添加权限</el-button
        >
      </div>
      <div>
        <el-table :data="dataList" row-key="powerId" :height="tabelHeight">
          <el-table-column label="编号" width="100">
            <template #default="{ row }">
              <span>{{ row.powerId }}</span>
            </template>
          </el-table-column>
          <el-table-column label="权限字段">
            <template #default="{ row }">
              <span class="font-semibold text-[18px]">{{ row.powerCode }}</span>
            </template>
          </el-table-column>
          <el-table-column label="权限名称">
            <template #default="{ row }">
              <span>{{ row.powerName }}</span>
            </template>
          </el-table-column>
          <el-table-column label="操作" width="400">
            <template #default="{ row }">
              <el-button
                :icon="CirclePlusFilled"
                type="primary"
                @click="addChildren(row)"
                >添加子权限</el-button
              >
              <el-button :icon="EditPen" type="warning" @click="set(row)"
                >编辑</el-button
              >
              <el-button :icon="Delete" type="danger" @click="del(row)"
                >删除</el-button
              >
            </template>
          </el-table-column>
        </el-table>
      </div>
    </el-card>
    <el-dialog
      v-model="AuthorityDialog.show"
      :title="(AuthorityDialog.usage.AorU ? '修改' : '新增') + '权限项'"
      width="20%"
    >
      <el-form
        ref="authorityDialogRef"
        :model="AuthorityDialog.data"
        :rules="AuthorityDialog.rules"
      >
        <el-form-item label="权限字段" prop="accessPath">
          <el-input
            v-model="AuthorityDialog.data.powerCode"
            placeholder="请填写权限字段"
          />
        </el-form-item>
        <el-form-item label="权限名称" prop="accessName">
          <el-input
            v-model="AuthorityDialog.data.powerName"
            placeholder="请填写权限名称"
          />
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="AuthorityDialog.show = false">取消</el-button>
          <el-button type="primary" @click="toAuthority(authorityDialogRef)">
            确认
          </el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { onMounted, reactive, ref } from "vue";
import {
  type Power,
  postAuthorityPack,
  getByIdPack,
  delPowerById
} from "@/api/power/index";
import { getTreeAll } from "@/api/power/index";
import { message } from "@/utils/message";
import { ElTree, FormInstance, ElMessageBox } from "element-plus";
import {
  Delete,
  EditPen,
  CirclePlusFilled,
  CirclePlus
} from "@element-plus/icons-vue";
import chessPawn from "@iconify-icons/twemoji/chess-pawn";

const dataList = ref<Array<Power>>([]);
const AuthorityDialog = reactive<iElDiaLog>({
  show: false,
  data: null,
  usage: {
    AorU: false,
    supId: null
  },
  rules: {
    powerName: [{ required: true, message: "请填写权限名称", trigger: "blur" }],
    powerCode: [{ required: true, message: "请填写权限字段", trigger: "blur" }]
  }
});
const authorityDialogRef = ref();
const tabelHeight = ref<number>();

onMounted(() => {
  getCardDetailsTableHeight();
  getDataList();
});

const add = () => {
  AuthorityDialog.data = {
    powerId: null,
    powerSupId: 0,
    powerCode: null,
    powerName: null
  };
  AuthorityDialog.show = true;
};

const addChildren = (row: Power) => {
  AuthorityDialog.data = {
    powerId: null,
    powerSupId: row.powerId,
    powerCode: null,
    powerName: null
  };
  AuthorityDialog.show = true;
};

const set = (row: Power) => {
  getByIdPack(
    row.powerId,
    res => {
      AuthorityDialog.data = res.data;
      AuthorityDialog.usage.AorU = true;
      AuthorityDialog.show = true;
      getDataList();
    },
    res => {
      message("获取权限信息失败" + res.msg, { type: "error" });
    }
  );
};

const del = (row: Power) => {
  ElMessageBox.confirm(
    "确认要删除该权限项吗?可能会影响系统的正常使用",
    "提示",
    {
      confirmButtonText: "确认",
      cancelButtonText: "取消",
      type: "warning"
    }
  )
    .then(() => {
      delPowerById(row.powerId)
        .then((res: R<any>) => {
          if (res.code == 200) {
            message("删除成功", { type: "success" });
          } else {
            message("删除失败" + res.msg, { type: "error" });
          }
        })
        .catch(error => {
          console.error("删除时遇到异常", error);
          message("删除失败", { type: "error" });
        })
        .finally(() => {
          getDataList();
        });
    })
    .catch(() => {
      message("取消操作");
    });
};

const getDataList = () => {
  getTreeAll()
    .then((res: R<Array<Power>>) => {
      if (res.code == 200) {
        dataList.value = res.data;
      } else {
        message("获取权限列表失败" + res.code, { type: "error" });
      }
    })
    .catch(error => {
      console.error("获取权限列表异常", error);
    });
};

const toAuthority = async (elForm: FormInstance | undefined) => {
  if (!elForm) return;
  await elForm.validate((valid, fields) => {
    if (valid) {
      if (!AuthorityDialog.usage.AorU) {
        postAuthorityPack(
          AuthorityDialog.data,
          () => {
            message("添加权限项成功", { type: "success" });
            getDataList();
            AuthorityDialog.show = false;
          },
          () => {
            message("添加权限项失败", { type: "error" });
          }
        );
      } else {
        postAuthorityPack(
          AuthorityDialog.data,
          () => {
            message("编辑权限项成功", { type: "success" });
            getDataList();
            AuthorityDialog.show = false;
          },
          () => {
            message("编辑权限项失败", { type: "error" });
          }
        );
      }
    } else {
      console.log("无法通过验证", fields);
    }
  });
};
/**
 * 动态计算表格高度
 **/
function getCardDetailsTableHeight() {
  const tableH = 200; //距离页面下方的高度
  const tableHeight = window.innerHeight - tableH;
  if (tableHeight <= 100) {
    tabelHeight.value = 100;
  } else {
    tabelHeight.value = window.innerHeight - tableH;
  }
}

// 监听窗口变化动态设置表格高度
window.onresize = () => {
  getCardDetailsTableHeight();
};
</script>

<style lang="scss" scoped>
span {
  font-family: "Cascadia Code", Consolas, "Courier New";
}
</style>
