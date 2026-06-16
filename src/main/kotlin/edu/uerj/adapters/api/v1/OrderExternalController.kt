package edu.uerj.adapters.api.v1

import edu.uerj.adapters.api.dto.ErrorDTO
import edu.uerj.adapters.api.dto.order.NewOrderDTO
import edu.uerj.adapters.api.dto.order.OrderCompleteDTO
import edu.uerj.domain.enums.OrderStatus
import edu.uerj.services.OrderService
import io.quarkus.arc.properties.IfBuildProperty
import jakarta.inject.Inject
import jakarta.ws.rs.Consumes
import jakarta.ws.rs.PATCH
import jakarta.ws.rs.POST
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
@IfBuildProperty(name = "modular-bff.api.mode", stringValue = "external")
class OrderExternalController @Inject
constructor(
    private val orderService: OrderService,
) {
    @Path("/create")
    @APIResponses(
        value = [
            APIResponse(
                responseCode = "201",
                description = "returned created Order",
                content = [Content(schema = Schema(implementation = OrderCompleteDTO::class))],
            ),
            APIResponse(
                responseCode = "400",
                description = "Error when create order",
                content = [Content(schema = Schema(implementation = ErrorDTO::class))],
            ),
            APIResponse(
                responseCode = "500",
                description = "Internal server error",
                content = [Content(schema = Schema(implementation = ErrorDTO::class))],
            ),
        ],
    )
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    fun createOrder(newOrder: NewOrderDTO): Response {
        try {
            if (newOrder.items.isEmpty()) {
                return Response
                    .status(Response.Status.BAD_REQUEST)
                    .entity(ErrorDTO("The order contains no items to be processed."))
                    .build()
            }

            if (newOrder.paymentMethods.isEmpty()) {
                return Response
                    .status(Response.Status.BAD_REQUEST)
                    .entity(ErrorDTO("The order has no payments to be processed."))
                    .build()
            }
            val createdOrder = orderService.createOrder(newOrder)
            return Response
                .status(Response.Status.CREATED)
                .entity(OrderCompleteDTO(createdOrder))
                .build()
        } catch (exception: IllegalArgumentException) {
            return Response
                .status(Response.Status.BAD_REQUEST)
                .entity(ErrorDTO(exception.message))
                .build()
        } catch (exception: Exception) {
            return Response
                .status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity(ErrorDTO(exception.message))
                .build()
        }
    }
}