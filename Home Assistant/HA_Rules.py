import csv
import random
import json
import threading
from datetime import datetime
try:
    import thread
except ImportError:
    import _thread as thread
import time
import requests
from nltk.stem import PorterStemmer
from nltk.tokenize import word_tokenize
sys_start = datetime.now()

ps = PorterStemmer()

with open("HA_message.json", "r") as f:
    HA = json.load(f)
headers = {
    "Authorization": HA["Authorization"],
    "content-type": HA["content-type"]
}


def is_number(s):
    try:
        float(s)
    except ValueError:
        return False
    else:
        return True


with open("vocabularies.json", "r") as f:
    vocabulary = json.load(f)

condition_below = vocabulary["condition_below"]
condition_above = vocabulary["condition_above"]

time_start = vocabulary["time_start"]
time_end = vocabulary["time_end"]

bool_true = vocabulary["bool_true"]
bool_false = vocabulary["bool_false"]
bool_not = vocabulary["bool_not"]

condition_above_set = set()
condition_below_set = set()
time_start_set = set()
time_end_set = set()
bool_not_set = set()
bool_true_set = set()
bool_false_set = set()


for item in condition_above:
    v = ps.stem(item)
    condition_above_set.add(v)
for item in condition_below:
    v = ps.stem(item)
    condition_below_set.add(v)
for item in time_start:
    v = ps.stem(item)
    time_start_set.add(v)
for item in time_end:
    v = ps.stem(item)
    time_end_set.add(v)
for item in bool_not:
    v = ps.stem(item)
    bool_not_set.add(v)
for item in bool_true:
    v = ps.stem(item)
    bool_true_set.add(v)
for item in bool_false:
    v = ps.stem(item)
    bool_false_set.add(v)


with open("entities.json", "r") as f:
    entities = json.load(f)


region_entities = dict()

name_entities = dict()

bool_entities = set()

number_entities = set()
eid_name_map = dict()

# print(entities)
for entity in entities:
    eid_name_map.setdefault(entity["entity_id"], entity["name"])
    w = entity["name"].split(" ")
    if "sensor" in w:
        w.remove("sensor")
    n = ps.stem("".join(w))
    if n not in name_entities:
        name_entities[n] = set()
    name_entities[n].add(entity["entity_id"])

    if "value_type" in entity:
        if entity["value_type"] == "bool":
            bool_entities.add(entity["entity_id"])
        elif entity["value_type"] == "number":
            number_entities.add(entity["entity_id"])
        else:
            exec("value type config error")
    if "region" in entity:
        r = ps.stem(entity["region"])
        if r not in region_entities:
            region_entities[r] = set()
        region_entities[r].add(entity["entity_id"])

# print(name_entities)
# print(value_entities)
# print(region_entities)


with open("rules.txt", "r") as f:
    __rules = f.read().split("\n")

rules = []

for __rule in __rules:

    if len(__rule) == 0:
        continue
    words = word_tokenize(__rule)
    if words[0] != "IF":
        exec("Error" + __rule)
    words.pop()

    __rule_t = {
        "trigger": list(),
        "action": dict(),
        "busy": "false"
    }
    __msg = dict()
    for i, word in enumerate(words):
        if "text" not in __msg:
            __msg.setdefault("text", list())
        __msg["text"].append(word)
        if word == "IF":
            continue
        elif word in "AND" or word in "THEN":
            if len(__msg) != 0:
                __rule_t["trigger"].append(__msg)
                __msg = dict()
            else:
                exec("The rule is unrecognizable: " + __rule)
                break
        else:
            if "G" == word:
                __msg.setdefault("MT", "G")
                continue
            if "F" == word:
                __msg.setdefault("MT", "F")
                continue
            if "," in word:
                __msg.setdefault("duration", int(word[2:]))
                continue
            if len(word) == 5 and word[2] == ":":
                if "time" not in __msg:
                    __msg.setdefault("time", list())
                __msg["time"].append(time.strptime(word, "%H:%M"))
                continue
            if word == "send":
                __msg.setdefault("notify", __rule)
                continue
            if is_number(word):
                if "number" not in __msg:
                    __msg.setdefault("number", list())
                __msg["number"].append(float(word))
                continue

            w = ps.stem(word)
            # print(w)
            if w in name_entities:
                __msg.setdefault("name", name_entities[w])
            if w in region_entities:
                __msg.setdefault("region", region_entities[w])
            if w in bool_not_set:
                __msg.setdefault("condition", "not")
                continue
            if w in bool_true_set:
                __msg.setdefault("bool", "true")
                continue
            if w in bool_false_set:
                __msg.setdefault("bool", "false")
                continue
            if w in condition_below_set:
                __msg.setdefault("condition", "below")
                continue
            if w in condition_above_set:
                __msg.setdefault("condition", "above")
                continue
            if w in time_start_set:
                __msg.setdefault("time_c", "start")
                continue
            if w in time_end_set:
                __msg.setdefault("time_c", "end")
                continue


    if len(__msg) != 0:
        __rule_t["action"] = __msg
        __msg = dict()
    else:
        exec("The rule is unrecognizable: " + __rule)
        break
    # print(__rule_t)
    for t in __rule_t["trigger"]:
        if "name" in t:
            if "number" in t:
                if len(t["number"]) != 2 and "condition" not in t:
                    exec("ERROR" + str(t))
            if "region" in t:
                __entities = t["name"] & t["region"]
                if len(__entities) == 0:
                    exec("ERROR" + str(t))
                else:
                    t["entities"] = __entities
            else:
                t["entities"] = t["name"]
        elif "time" in t:
            if len(t["time"]) != 2 and "time_c" not in t:
                exec("ERROR" + str(t))
        elif "notify" in t:
            pass
        else:
            exec("ERROR" + str(t))

    if "name" in __rule_t["action"]:
        if "number" in __rule_t["action"]:
            if len(__rule_t["action"]["number"]) != 2 and "condition" not in __rule_t["action"]:
                exec("ERROR" + str(__rule_t["action"]))
        if "region" in __rule_t["action"]:
            __entities = __rule_t["action"]["name"] & __rule_t["action"]["region"]
            if len(__entities) == 0:
                exec("ERROR" + str(__rule_t["action"]))
            else:
                __rule_t["action"]["entities"] = __entities
        else:
            __rule_t["action"]["entities"] = __rule_t["action"]["name"]
    elif "time" in __rule_t["action"]:
        if len(__rule_t["action"]["time"]) != 2 and "time_c" not in __rule_t["action"]:
            exec("ERROR" + str(__rule_t["action"]))
    elif "notify" in __rule_t["action"]:
        pass
    else:
        exec("ERROR" + str(__rule_t["action"]))
    rules.append(__rule_t)

class Condition:
    def __init__(self, eid, value_type):
        self.entity_id = eid
        self.value_type = value_type
        self.value_bool = "off"
        self.value_above = float("-inf")
        self.value_below = float("inf")
        self.time_start_h = 0
        self.time_start_m = 0
        self.time_end_h = 23
        self.time_end_m = 59
        self.last_state = False
        self.last_changed = time.localtime(time.time())
        self.last_fired = time.localtime(time.time())
        self.rules = list()

    def judge(self, value):
        if self.value_type == 0:
            if self.value_bool == value:
                if not self.last_state:
                    self.last_changed = time.localtime(time.time())
                self.last_state = True
                self.last_fired = time.localtime(time.time())
                return True
            else:
                self.last_state = False
        elif self.value_type == 1:
            if self.value_above <= value <= self.value_below:
                if not self.last_state:
                    self.last_changed = time.localtime(time.time())
                self.last_state = True
                self.last_fired = time.localtime(time.time())
                return True
            else:
                self.last_state = False
        else:
            if self.time_start_h < value["h"] < self.time_end_h:
                if not self.last_state:
                    self.last_changed = time.localtime(time.time())
                self.last_state = True
                self.last_fired = time.localtime(time.time())
                return True
            elif self.time_start_h <= value["h"] <= self.time_end_h:
                if self.time_start_m <= value["m"] <= self.time_end_m:
                    if not self.last_state:
                        self.last_changed = time.localtime(time.time())
                    self.last_state = True
                    self.last_fired = time.localtime(time.time())
                    return True
                else:
                    self.last_state = False
            else:
                self.last_state = False
        return False


class Action:
    def __init__(self, eid, service):
        self.target = eid
        self.domain = eid.split(".")[0]
        self.free = True
        self.next_free_time = time.localtime(time.time())
        self.service = service
        self.message = "remainder" + str(random.randint(1, 100))

    def do(self):
        if self.service == "turn_on":
            if env[self.target] == "on":
                return
        service_data = None
        if self.target == "input_text.notify":
            service_data = {
                "entity_id": self.target,
                "value": self.message
            }
        else:
            service_data = {
                "entity_id": self.target
            }

        print(service_data)
        print(HA["url"] + "services/" + self.domain + "/" + self.service)
        requests.post(url=(HA["url"] + "services/" + self.domain + "/" + self.service), headers=headers,
                      data=json.dumps(service_data))
        print("do action")


def is_number(str):
    try:
        float(str)
    except ValueError:
        return False
    else:
        return True


env = dict()
conditions = list()
actions = list()
rt_rules = list()

for rid, _rule in enumerate(rules):
    rt_rule_t = dict()
    rt_rule_t["busy"] = False
    eid_t = None
    if "entities" in _rule["action"]:
        # print(_rule)
        eid_t = (_rule["action"])["entities"].pop()
        (_rule["action"])["entities"].add(eid_t)
    action_t = None
    if "bool" in _rule["action"]:
        if _rule["action"]["bool"] == "true":
            action_t = Action(eid_t, "turn_on")
        else:
            action_t = Action(eid_t, "turn_off")
        aid = len(actions)
        actions.append(action_t)
        rt_rule_t["aid"] = aid
    elif "notify" in _rule["action"]:
        action_t = Action("input_text.notify", "set_value")
        aid = len(actions)
        actions.append(action_t)
        rt_rule_t["aid"] = aid
    else:
        exec("ERROR")

    rt_rule_t["triggers"] = list()

    for tid, _trigger in enumerate(_rule["trigger"]):
        if "entities" in _trigger:
            eid_t = _trigger["entities"].pop()
            _trigger["entities"].add(eid_t)
        _condition = None
        if "bool" in _trigger and "time" not in _trigger:
            _condition = Condition(eid_t, 0)
            if _trigger["bool"] == "true" and "condition" not in _trigger:
                _condition.value_bool = "on"
            else:
                _condition.value_bool = "off"
            if tid == 0:
                _condition.rules.append(rid)
            cid = len(conditions)
            conditions.append(_condition)
            if "MT" in _trigger:
                if _trigger["MT"][0] == "G":
                    rt_rule_t["triggers"].append({
                        "cid": cid,
                        "mt": {
                            "type": 0,
                            "duration": _trigger["duration"]
                        }
                    })
                if _trigger["MT"][0] == "F":
                    rt_rule_t["triggers"].append({
                        "cid": cid,
                        "mt": {
                            "type": 1,
                            "duration": _trigger["duration"]
                        }
                    })
            else:
                rt_rule_t["triggers"].append({
                    "cid": cid
                })
        elif "number" in _trigger:
            _condition = Condition(eid_t, 1)
            if _trigger["condition"] == "above":
                _condition.value_above = _trigger["number"][0]
            else:
                _condition.value_below = _trigger["number"][0]
            if tid == 0:
                _condition.rules.append(rid)
            cid = len(conditions)
            conditions.append(_condition)
            if "MT" in _trigger:
                if _trigger["MT"][0] == "G":
                    rt_rule_t["triggers"].append({
                        "cid": cid,
                        "mt": {
                            "type": 0,
                            "duration": _trigger["duration"]
                        }
                    })
                if _trigger["MT"][0] == "F":
                    rt_rule_t["triggers"].append({
                        "cid": cid,
                        "mt": {
                            "type": 1,
                            "duration": _trigger["duration"]
                        }
                    })
            else:
                rt_rule_t["triggers"].append({
                    "cid": cid
                })
        else:
            _condition = Condition(None, 2)
            _h = _trigger["time"][0].tm_hour
            _m = _trigger["time"][0].tm_min
            if _trigger["time_c"] == "start":
                _condition.time_start_h = _h
                _condition.time_start_m = _m
            else:
                _condition.time_end_h = _h
                _condition.time_end_m = _m
            if tid == 0:
                _condition.rules.append(rid)
            cid = len(conditions)
            conditions.append(_condition)
            rt_rule_t["triggers"].append({
                "cid": cid
            })
    rt_rules.append(rt_rule_t)

def g_task(cid, d, last_changed, rid):
    print(d)
    time.sleep(d)
    _c = conditions[cid]
    if _c.judge(env[_c.entity_id]):
        if _c.last_changed == last_changed:
            rt_rules[rid]["trigger_num"] = rt_rules[rid]["trigger_num"] + 1
    print(d, "end")


def f_task(cid, d, rid):
    start_time = time.localtime(time.time())
    time.sleep(d)
    _c = conditions[cid]
    if _c.judge(env[_c.entity_id]):
        rt_rules[rid]["trigger_num"] = rt_rules[rid]["trigger_num"] + 1
    else:
        if start_time <= _c.last_fired <= time.localtime(time.time()):
            rt_rules[rid]["trigger_num"] = rt_rules[rid]["trigger_num"] + 1


def do_action(rid):
    actions[rt_rules[rid]["aid"]].do()


def trigger_rule(rid):
    if rt_rules[rid]["busy"] == True:
        print("ERROR")
        return
    rt_rules[rid]["busy"] = True
    rt_rules[rid]["trigger_sum"] = len(rt_rules[rid]["triggers"])
    rt_rules[rid]["trigger_num"] = 0
    _ttt = list()
    for _t in rt_rules[rid]["triggers"]:
        # print(_t)
        _c = conditions[_t["cid"]]
        if _c.value_type == 0 or _c.value_type == 1:
            if _c.judge(env[_c.entity_id]):
                if "mt" in _t:
                    if _t["mt"]["type"] == 0:
                        _g_task = threading.Thread(target=g_task,
                                                   args=(_t["cid"], _t["mt"]["duration"], _c.last_changed, rid))
                        _g_task.start()
                        _ttt.append(_g_task)
                    else:
                        rt_rules[rid]["trigger_num"] = rt_rules[rid]["trigger_num"] + 1
                else:
                    rt_rules[rid]["trigger_num"] = rt_rules[rid]["trigger_num"] + 1
            else:
                if "mt" in _t:
                    if _t["mt"]["type"] == 1:
                        _f_task = threading.Thread(target=f_task, args=(_t["cid"], _t["mt"]["duration"], rid))
                        _f_task.start()
                        _ttt.append(_f_task)
                else:
                    break
        else:
            _time_now = time.localtime(time.time())
            if _c.judge({
                "h": _time_now.tm_hour,
                "m": _time_now.tm_min
            }):
                rt_rules[rid]["trigger_num"] = rt_rules[rid]["trigger_num"] + 1
            else:
                break
    for _t in _ttt:
        _t.join()
    if rt_rules[rid]["trigger_sum"] == rt_rules[rid]["trigger_num"]:
        print("ERROR")
        do_action(rid)
    else:
        pass

    rt_rules[rid]["busy"] = False


res = requests.get(url=(HA["url"] + "states"), headers=headers)
result = res.json()


def is_in_entities(e):
    for entity in entities:
        if e in entity["entity_id"]:
            return True
    return False



for item in result:
    if "entity_id" in item:
        if is_in_entities(item["entity_id"]):
            # print(i["entity_id"])
            if is_number(item["state"]):
                item["state"] = float(item["state"])
            env.setdefault(item["entity_id"], item["state"])

for e in env:
    for _c in conditions:
        if _c.entity_id == e:
            _c.judge(env[e])


def trigger_condition(state_change_message):
    if is_number(state_change_message["state"]):
        state_change_message["state"] = float(state_change_message["state"])

    _ttt = list()
    for _c in conditions:
        if _c.entity_id == state_change_message["entity_id"]:
            if _c.judge(state_change_message["state"]):

                for rid in _c.rules:
                    print("ERROR")
                    _t = threading.Thread(target=trigger_rule, args=(rid,))
                    _t.start()
                    _ttt.append(_t)
    for _t in _ttt:
        _t.join()


def trigger_time(time_change_message):
    print(time_change_message)
    for _c in conditions:
        if _c.value_type == 2:
            if _c.judge(time_change_message):
                for rid in _c.rules:
                    threading.Thread(target=trigger_rule, args=(rid,)).start()


def run_loop():
    state_change_message = dict()

    for i in range(250):
        for eid in env:
            suffix = "states/" + eid
            res = requests.get(url=(HA["url"] + suffix), headers=headers)
            res_data = res.json()

            if is_number(res_data["state"]):
                res_data["state"] = float(res_data["state"])
            state_change_message.setdefault("entity_id", eid)
            state_change_message.setdefault("state", res_data["state"])
            env[eid] = res_data["state"]
    trigger_condition(state_change_message)

    return


    while True:
        time.sleep(1)
        time_change_message = {
            "h": time.localtime(time.time()).tm_hour,
            "m": time.localtime(time.time()).tm_min
        }


        trigger_time(time_change_message)

        row = list()
        row.append(time.strftime("%H:%M:%S", time.localtime()))
        for eid in env:
            suffix = "states/" + eid
            res = requests.get(url=(HA["url"] + suffix), headers=headers)
            # print(res.json())
            res_data = res.json()
            if len(row) != 0:
                row.append(res_data["state"])

            if is_number(res_data["state"]):
                res_data["state"] = float(res_data["state"])

            if env[eid] != res_data["state"]:
                state_change_message = dict()
                state_change_message.setdefault("entity_id", eid)
                state_change_message.setdefault("state", res_data["state"])
                trigger_condition(state_change_message)
                env[eid] = res_data["state"]


try:
    run_loop()
except InterruptedError:
    pass

