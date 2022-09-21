package de.intelligence.bsod;

import jdk.incubator.foreign.CLinker;
import jdk.incubator.foreign.FunctionDescriptor;
import jdk.incubator.foreign.ResourceScope;
import jdk.incubator.foreign.SegmentAllocator;
import jdk.incubator.foreign.SymbolLookup;

import static jdk.incubator.foreign.ValueLayout.*;

public final class PanamaBSOD {

    public static void main(String[] args) {
        System.loadLibrary("ntdll");
        try (ResourceScope scope = ResourceScope.newConfinedScope()) {
            final SegmentAllocator allocator = SegmentAllocator.nativeAllocator(scope);
            CLinker.systemCLinker().downcallHandle(SymbolLookup.loaderLookup().lookup("RtlAdjustPrivilege").orElseThrow(),
                            FunctionDescriptor.of(JAVA_LONG, JAVA_INT, JAVA_BOOLEAN, JAVA_BOOLEAN, ADDRESS))
                    .invoke(19, true, false, allocator.allocate(JAVA_BOOLEAN));
            CLinker.systemCLinker().downcallHandle(SymbolLookup.loaderLookup().lookup("NtRaiseHardError").orElseThrow(),
                            FunctionDescriptor.of(JAVA_LONG, JAVA_LONG, JAVA_INT, JAVA_INT, JAVA_INT, JAVA_INT, ADDRESS))
                    .invoke(0xEEEEEEEEL, 0, 0, 0, 6, allocator.allocate(JAVA_LONG));
        } catch (Throwable ignored) {}
    }

}
