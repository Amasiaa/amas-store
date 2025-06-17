package mg.amas.data.model.response

import kotlinx.serialization.Serializable
import mg.amas.domain.model.CartSummary

@Serializable
data class CartSummaryResponse(
    val `data`: Summary,
    val msg: String,
) {
    fun toCartSummary() =
        CartSummary(
            summaryData = `data`.toSummaryData(),
            msg = msg,
        )
}
