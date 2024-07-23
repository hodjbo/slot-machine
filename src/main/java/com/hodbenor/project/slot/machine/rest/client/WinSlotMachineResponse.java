package com.hodbenor.project.slot.machine.rest.client;

import java.util.Map;

public record WinSlotMachineResponse(Map<String, Integer> rewards) {
}
