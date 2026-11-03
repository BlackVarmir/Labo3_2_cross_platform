package ua.edu.chnu.labo3

import ua.edu.chnu.labo3.data.NetworkResult
import ua.edu.chnu.labo3.data.fold
import ua.edu.chnu.labo3.data.map
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertSame

class NetworkResultMapTest {

    @Test
    fun mapTransformsSuccessPayload() {
        val result: NetworkResult<Int> = NetworkResult.Success(21)

        val mapped = result.map { it * 2 }

        assertEquals(NetworkResult.Success(42), mapped)
    }

    @Test
    fun mapKeepsErrorUntouched() {
        val error = NetworkResult.Error("boom")
        val result: NetworkResult<Int> = error

        val mapped = result.map { it * 2 }

        assertSame(error, mapped)
    }

    @Test
    fun mapTurnsThrowingTransformIntoError() {
        val result: NetworkResult<Int> = NetworkResult.Success(1)

        val mapped = result.map<Int, Int> { error("bad mapping") }

        val error = assertIs<NetworkResult.Error>(mapped)
        assertEquals("bad mapping", error.message)
    }

    @Test
    fun foldHandlesBothBranches() {
        val ok: NetworkResult<Int> = NetworkResult.Success(5)
        val bad: NetworkResult<Int> = NetworkResult.Error("nope")

        assertEquals("ok 5", ok.fold(onSuccess = { "ok $it" }, onError = { "err $it" }))
        assertEquals("err nope", bad.fold(onSuccess = { "ok $it" }, onError = { "err $it" }))
    }
}
