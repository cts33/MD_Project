import java.util.*


fun main() {
    System.out.println("----")
    val nums1 = intArrayOf(1, 2, 3, 0, 0, 0)

    val nums2 = intArrayOf(2, 5, 6)
    merge(nums1, 3, nums2, 3)
}

/**
 *  两数之和
 * 输入：nums = [2,7,11,15], target = 9
输出：[0,1]
解释：因为 nums[0] + nums[1] == 9 ，返回 [0, 1] 。
 */
fun twoSum(nums: IntArray, target: Int): IntArray {
    val hashtable: MutableMap<Int, Int> = HashMap()
    for (i in nums.indices) {
        if (hashtable.containsKey(target - nums[i])) {
            return intArrayOf(hashtable[target - nums[i]]!!, i)
        }
        hashtable[nums[i]] = i
    }
    return IntArray(0)
}

fun maxSubArray(nums: IntArray): Int {
    var maxValue = nums[0]
    for (index in 0..nums.size) {


        var temp = nums[index]
        if (temp <= 0) {
            continue
        }
        for (pos in index..nums.size) {
            temp += nums[pos]
            maxValue = Math.max(temp, maxValue)
        }
    }
    return maxValue
}

fun merge(nums1: IntArray, m: Int, nums2: IntArray, n: Int): Unit {
    val xArray = IntArray(m + n)

    for (item in 0 until m) {
        xArray[item] = nums1[item]
    }
    for (item in 0 until n) {
        xArray[item + m] = nums2[item]
    }

    Arrays.sort(xArray)
    for (item in xArray) {

        System.out.println("----${item}")
    }
}