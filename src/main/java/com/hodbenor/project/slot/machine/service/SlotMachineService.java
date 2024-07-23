package com.hodbenor.project.slot.machine.service;

import com.hodbenor.project.slot.machine.data.beans.User;
import com.hodbenor.project.slot.machine.service.beans.SpinResult;

public interface SlotMachineService {

    SpinResult spin(User user);
}
