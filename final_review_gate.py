# final_review_gate.py
import sys
import os

if __name__ == "__main__":
    # 尝试将标准输出设置为非缓冲模式，以实现更灵敏的交互。
    # 这在某些平台或非终端环境中可能无效，但推荐这样做。
    try:
        sys.stdout = os.fdopen(sys.stdout.fileno(), 'w', buffering=1)
    except Exception:
        pass  # 如果失败（例如在某些环境下），则忽略

    try:
        sys.stderr = os.fdopen(sys.stderr.fileno(), 'w', buffering=1)
    except Exception:
        pass  # 同上

    print("--- 最终审查入口已激活 ---", flush=True)
    print("AI 已完成主要任务，正在等待你的审查或补充指令。", flush=True)
    print("请输入补充指令，或者输入以下任一关键词表示完成：'TASK_COMPLETE', 'Done', 'Quit', 'q'", flush=True)

    active_session = True
    while active_session:
        try:
            # 显示脚本已准备好接收输入。
            print("REVIEW_GATE_AWAITING_INPUT:", end="", flush=True)

            line = sys.stdin.readline()

            if not line:  # 文件结束（EOF）
                print("--- 审查入口：输入通道关闭（EOF），退出脚本 ---", flush=True)
                active_session = False
                break

            user_input = line.strip()

            # 判断退出条件
            if user_input.upper() in ['TASK_COMPLETE', 'DONE', 'QUIT', 'Q']:
                print(f"--- 审查入口：用户通过 '{user_input.upper()}' 表示任务完成 ---", flush=True)
                active_session = False
                break
            elif user_input:
                # AI 将"监听"以下格式的输出
                print(f"USER_REVIEW_SUB_PROMPT: {user_input}", flush=True)
            # 如果用户输入为空但不是退出命令，则继续循环等待
        except KeyboardInterrupt:
            print("--- 审查入口：用户中断会话（Ctrl+C） ---", flush=True)
            active_session = False
            break
        except Exception as e:
            print(f"--- 审查入口脚本错误：{e} ---", flush=True)
            active_session = False
            break

    print("--- 最终审查脚本已退出 ---", flush=True)