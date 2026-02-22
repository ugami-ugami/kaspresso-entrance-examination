package ru.webrelab.kie.cerealstorage

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class CerealStorageImplTest {

    private val storage = CerealStorageImpl(10f, 20f)

    /**
     * ============================
     * Constructor validation
     * ============================
     */

    @Test
    fun `should throw if containerCapacity is negative`() {
        assertThrows(IllegalArgumentException::class.java) {
            CerealStorageImpl(-4f, 10f)
        }
    }

    @Test
    fun `should throw if storageCapacity less than containerCapacity`() {
        assertThrows(IllegalArgumentException::class.java) {
            CerealStorageImpl(20f, 10f)
        }
    }

    @Test
    fun `should create storage successfully`() {
        assertDoesNotThrow {
            CerealStorageImpl(10f, 20f)
        }
    }

    /**
     * ============================
     * addCereal()
     * ============================
     */

    @Test
    fun `should throw if addCereal amount is negative`() {
        assertThrows(IllegalArgumentException::class.java) {
            storage.addCereal(Cereal.BUCKWHEAT, -1f)
        }
    }

    @Test
    fun `should allow addCereal with zero amount`() {
        val remainder = storage.addCereal(Cereal.RICE, 0f)

        assertEquals(0f, remainder, 0.01f)
        assertEquals(0f, storage.getAmount(Cereal.RICE), 0.01f)
    }

    @Test
    fun `should add cereal to new container without overflow`() {
        val remainder = storage.addCereal(Cereal.BUCKWHEAT, 9f)

        assertEquals(9f, storage.getAmount(Cereal.BUCKWHEAT), 0.01f)
        assertEquals(0f, remainder, 0.01f)
    }

    @Test
    fun `should add cereal to new container with overflow`() {
        val remainder = storage.addCereal(Cereal.PEAS, 11f)

        assertEquals(10f, storage.getAmount(Cereal.PEAS), 0.01f)
        assertEquals(1f, remainder, 0.01f)
    }

    @Test
    fun `should add cereal to existing container with overflow`() {
        val firstRemainder = storage.addCereal(Cereal.MILLET, 8f)
        val secondRemainder = storage.addCereal(Cereal.MILLET, 5f)

        assertEquals(0f, firstRemainder, 0.01f)
        assertEquals(10f, storage.getAmount(Cereal.MILLET), 0.01f)
        assertEquals(3f, secondRemainder, 0.01f)
    }

    @Test
    fun `should throw when trying to create third container`() {
        storage.addCereal(Cereal.RICE, 1f)
        storage.addCereal(Cereal.BUCKWHEAT, 1f)

        assertThrows(IllegalStateException::class.java) {
            storage.addCereal(Cereal.MILLET, 1f)
        }
    }

    /**
     * ============================
     * getCereal()
     * ============================
     */

    @Test
    fun `should throw if getCereal amount is negative`() {
        assertThrows(IllegalArgumentException::class.java) {
            storage.getCereal(Cereal.MILLET, -1f)
        }
    }

    @Test
    fun `should return zero when container does not exist`() {
        val result = storage.getCereal(Cereal.PEAS, 1f)

        assertEquals(0f, result, 0.01f)
    }

    @Test
    fun `should return requested amount when enough cereal in container`() {
        storage.addCereal(Cereal.RICE, 5f)

        val result = storage.getCereal(Cereal.RICE, 3f)

        assertEquals(3f, result, 0.01f)
        assertEquals(2f, storage.getAmount(Cereal.RICE), 0.01f)
    }

    /**
     * ============================
     * removeContainer()
     * ============================
     */

    @Test
    fun `should remove empty container and return true`() {
        storage.addCereal(Cereal.RICE, 4f)
        storage.getCereal(Cereal.RICE, 4f)

        val result = storage.removeContainer(Cereal.RICE)

        assertTrue(result)
        assertEquals(0f, storage.getAmount(Cereal.RICE), 0.01f)
    }

    /**
     * ============================
     * getAmount()
     * ============================
     */

    @Test
    fun `should return correct amount when container exists`() {
        storage.addCereal(Cereal.RICE, 6f)

        assertEquals(6f, storage.getAmount(Cereal.RICE), 0.01f)
    }

    /**
     * ============================
     * getSpace()
     * ============================
     */

    @Test
    fun `should return available space when container exists`() {
        storage.addCereal(Cereal.BULGUR, 6f)

        assertEquals(4f, storage.getSpace(Cereal.BULGUR), 0.01f)
    }

    /**
     * ============================
     * toString()
     * ============================
     */

    @Test
    fun `should return empty map string when storage is empty`() {
        assertEquals("{}", storage.toString())
    }

    @Test
    fun `should return correct string representation with one container`() {
        storage.addCereal(Cereal.RICE, 5f)

        assertEquals("{RICE=5.0}", storage.toString())
    }

    @Test
    fun `should return correct string representation with multiple containers`() {
        storage.addCereal(Cereal.RICE, 5f)
        storage.addCereal(Cereal.BUCKWHEAT, 3f)

        val result = storage.toString()

        assertTrue(result.contains("RICE=5.0"))
        assertTrue(result.contains("BUCKWHEAT=3.0"))
    }
}