package edu.uerj.adapters.api.v1

import edu.uerj.adapters.api.dto.ErrorDTO
import edu.uerj.adapters.api.dto.order.OrderCompleteDTO
import edu.uerj.domain.enums.OrderStatus
import edu.uerj.services.OrderService
import io.quarkus.arc.properties.IfBuildProperty
import jakarta.inject.Inject
import jakarta.ws.rs.PATCH
import jakarta.ws.rs.Path
import jakarta.ws.rs.PathParam
import jakarta.ws.rs.Produces
import jakarta.ws.rs.core.MediaType
import jakarta.ws.rs.core.Response
import org.eclipse.microprofile.openapi.annotations.media.Content
import org.eclipse.microprofile.openapi.annotations.media.Schema
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses

@Path("/v1/order")
@IfBuildProperty(name = "modular-bff.api.mode", stringValue = "internal")
class OrderInternalController
@Inject
constructor(
    private val orderService: OrderService,
){
    @Path("/{orderId}/delivery/{deliveryId}/{newStatus}")
    @APIResponses(
        value = [
            APIResponse(
                responseCode = "202",
                description = "returned updated Order",
                content = [Content(schema = Schema(implementation = OrderCompleteDTO::class))],
            ),
            APIResponse(
                responseCode = "400",
                description = "Error when update order",
                content = [Content(schema = Schema(implementation = ErrorDTO::class))],
            ),
            APIResponse(
                responseCode = "404",
                description = "Not found order or delivery",
                content = [Content(schema = Schema(implementation = ErrorDTO::class))],
            ),
            APIResponse(
                responseCode = "500",
                description = "Internal server error",
                content = [Content(schema = Schema(implementation = ErrorDTO::class))],
            ),
        ],
    )
    @PATCH
    @Produces(MediaType.APPLICATION_JSON)
    fun updateStatusOrder(
        @PathParam("orderId") orderId: Long,
        @PathParam("deliveryId") deliveryId: Long,
        @PathParam("newStatus") status: OrderStatus,
    ): Response {
        try {
            val order =
                orderService.updateStatus(orderId, deliveryId, status)
                    ?: return Response
                        .status(Response.Status.NOT_FOUND)
                        .entity(null)
                        .build()

            return Response
                .status(Response.Status.ACCEPTED)
                .entity(OrderCompleteDTO(order))
                .build()
        } catch (exception: IllegalArgumentException) {
            return Response
                .status(Response.Status.BAD_REQUEST)
                .entity(ErrorDTO(exception.message))
                .build()
        } catch (exception: NumberFormatException) {
            return Response
                .status(Response.Status.BAD_REQUEST)
                .entity(ErrorDTO("Invalid orderId or deliveryId"))
                .build()
        } catch (exception: Exception) {
            return Response
                .status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity(ErrorDTO(exception.message))
                .build()
        }
    }
}