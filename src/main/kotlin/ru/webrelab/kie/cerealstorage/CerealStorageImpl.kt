package ru.webrelab.kie.cerealstorage

class CerealStorageImpl(
    override val containerCapacity: Float,
    override val storageCapacity: Float
) : CerealStorage {

    init {
        require(containerCapacity > 0f) {
            "Container capacity must be greater than zero"
        }
        require(storageCapacity >= containerCapacity) {
            "Storage capacity must not be less than container capacity"
        }
    }

    private val storage = mutableMapOf<Cereal, Float>()

    override fun addCereal(cereal: Cereal, amount: Float): Float {
        require(amount >= 0f) { "Amount must not be negative" }

        if (!storage.containsKey(cereal)) {
            val maxContainers = (storageCapacity / containerCapacity).toInt()
            if (storage.size >= maxContainers) {
                throw IllegalStateException("No space for new container")
            }
            storage[cereal] = 0f
        }

        val currentAmount = storage[cereal] ?: 0f
        val spaceLeft = containerCapacity - currentAmount

        return if (amount <= spaceLeft) {
            storage[cereal] = currentAmount + amount
            0f
        } else {
            storage[cereal] = containerCapacity
            amount - spaceLeft
        }
    }

    override fun getCereal(cereal: Cereal, amount: Float): Float {
        require(amount >= 0f) { "Amount must not be negative" }

        val currentAmount = storage[cereal] ?: return 0f

        return if (amount <= currentAmount) {
            storage[cereal] = currentAmount - amount
            amount
        } else {
            storage[cereal] = 0f
            currentAmount
        }
    }

    override fun removeContainer(cereal: Cereal): Boolean {
        val currentAmount = storage[cereal] ?: return false

        return if (currentAmount == 0f) {
            storage.remove(cereal)
            true
        } else {
            false
        }
    }

    override fun getAmount(cereal: Cereal): Float {
        return storage[cereal] ?: 0f
    }

    override fun getSpace(cereal: Cereal): Float {
        val currentAmount = storage[cereal]
            ?: throw IllegalStateException("Container does not exist")

        return containerCapacity - currentAmount
    }

    override fun toString(): String {
        return storage.toString()
    }
}