package com.hodbenor.project.slot.machine.rest.client;

import java.util.List;

public record WinSlotMachineRequest(long userId, List<Integer> digits) {
}
