package com.hodbenor.project.slot.machine.rest;

import com.hodbenor.project.slot.machine.data.UserDal;
import com.hodbenor.project.slot.machine.service.SlotMachineService;
import com.hodbenor.project.slot.machine.service.beans.SpinResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/slot-machine")
public class SlotMachineController {

    private final SlotMachineService slotMachineService;
    private final UserDal userDal;

    @Autowired
    public SlotMachineController(SlotMachineService slotMachineService, UserDal userDal) {
        this.slotMachineService = slotMachineService;
        this.userDal = userDal;
    }

    @GetMapping(value = "/spin", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<SpinResult> spinSlotMachine(@RequestHeader long userId) {

        try {
            return ResponseEntity.ok(slotMachineService.spin(userDal.getUser(userId)));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
}
