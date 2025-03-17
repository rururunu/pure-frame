/*
 * @Author: 郭昊明 hmguo@cdtechsz.com
 * @Date: 2024-01-24 13:22:14
 * @LastEditors: 郭昊明 hmguo@cdtechsz.com
 * @LastEditTime: 2024-03-05 12:16:51
 * @FilePath: \nl_pure_admin\src\utils\nlTool.ts
 * @Description: nl工具箱
 */

/**
 * ! 时间格式化
 * ? 直接调用不填写可以获取当前时间的字符串
 * ? 注意 Y M D H m s 其中部分是大写
 * ? 默认输出 YYYY-MM-DD HH:mm:ss 格式
 * @param format 时间格式 默认当前时间
 * @param timestamp 时间选填
 * @returns
 */
export function dateFormat(
  timestamp: number | string | Date = new Date().getTime(),
  format = "YYYY-MM-DD HH:mm:ss"
): string {
  if (
    (typeof timestamp == "string" || typeof timestamp == "number") &&
    !isNaN(timestamp)
  ) {
    if (typeof timestamp == "string") {
      timestamp = Number(timestamp);
    }
    if (timestamp < 10000000000) {
      timestamp *= 1000;
    }
  }
  const date = new Date(timestamp);
  function fixedTwo(value: number): string {
    return value < 10 ? "0" + value : String(value);
  }
  let showTime = format;
  if (showTime.includes("SSS")) {
    const S = date.getMilliseconds();
    showTime = showTime.replace("SSS", "0".repeat(3 - String(S).length) + S);
  }
  if (showTime.includes("YY")) {
    const Y = date.getFullYear();
    showTime = showTime.includes("YYYY")
      ? showTime.replace("YYYY", String(Y))
      : showTime.replace("YY", String(Y).slice(2, 4));
  }
  if (showTime.includes("M")) {
    const M = date.getMonth() + 1;
    showTime = showTime.includes("MM")
      ? showTime.replace("MM", fixedTwo(M))
      : showTime.replace("M", String(M));
  }
  if (showTime.includes("D")) {
    const D = date.getDate();
    showTime = showTime.includes("DD")
      ? showTime.replace("DD", fixedTwo(D))
      : showTime.replace("D", String(D));
  }
  if (showTime.includes("H")) {
    const H = date.getHours();
    showTime = showTime.includes("HH")
      ? showTime.replace("HH", fixedTwo(H))
      : showTime.replace("H", String(H));
  }
  if (showTime.includes("m")) {
    const m = date.getMinutes();
    showTime = showTime.includes("mm")
      ? showTime.replace("mm", fixedTwo(m))
      : showTime.replace("m", String(m));
  }
  if (showTime.includes("s")) {
    const s = date.getSeconds();
    showTime = showTime.includes("ss")
      ? showTime.replace("ss", fixedTwo(s))
      : showTime.replace("s", String(s));
  }
  return showTime;
}

// 获取当天的开始时间
export function getStartOfDay() {
  const now = new Date();
  const startOfDay = new Date(now.getFullYear(), now.getMonth(), now.getDate());
  return startOfDay;
}

// 获取当天的结束时间
export function getEndOfDay() {
  const now = new Date();
  const endOfDay = new Date(
    now.getFullYear(),
    now.getMonth(),
    now.getDate(),
    23,
    59,
    59,
    999
  );
  return endOfDay;
}
