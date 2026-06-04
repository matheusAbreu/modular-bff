package org.acme.adapters.api.v1

import io.quarkus.runtime.annotations.ConfigPhase
import io.quarkus.runtime.annotations.ConfigRoot
import io.smallrye.config.ConfigMapping
import jakarta.inject.Inject
import jakarta.ws.rs.GET
import jakarta.ws.rs.Path
import jakarta.ws.rs.PathParam
import jakarta.ws.rs.Produces
import jakarta.ws.rs.core.MediaType
import jakarta.ws.rs.core.Response
import org.acme.adapters.api.dto.ErrorDTO
import org.acme.adapters.api.dto.OrderDTO
import org.acme.services.OrderService
import org.eclipse.microprofile.openapi.annotations.media.Content
import org.eclipse.microprofile.openapi.annotations.media.Schema
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses

@Path("/v1/order")
class OrderController
@Inject constructor(
    private val orderService: OrderService
) {

    @Path("/{orderId}")
    @APIResponses(
        value = [
            APIResponse(
                responseCode = "200",
                description = "returned find Order",
                content = [Content(schema = Schema(implementation = OrderDTO::class))]
            ),
            APIResponse(
                responseCode = "404",
                description = "Order not found",
                content = [Content(schema = Schema(implementation = ErrorDTO::class))]
            ),
            APIResponse(
                responseCode = "500",
                description = "Internal server error",
                content = [Content(schema = Schema(implementation = ErrorDTO::class))]
            ),
        ]
    )
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    fun getOrderById(
        @PathParam("orderId") orderId: String,
    ): Response {
        try {
            val order =
                orderService.getOrder(orderId.toLong()) ?: return Response.
                status(Response.Status.NOT_FOUND)
                    .entity(ErrorDTO("Order with id $orderId not found"))
                    .build()

            val dto = OrderDTO(order)
            return Response.status(Response.Status.OK).entity(dto).build()
        } catch (exception: Exception){
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity(ErrorDTO(exception.message)).build()
        }
    }
}