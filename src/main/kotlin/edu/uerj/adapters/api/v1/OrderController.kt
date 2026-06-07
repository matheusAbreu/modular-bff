package edu.uerj.adapters.api.v1

import edu.uerj.adapters.api.dto.ErrorDTO
import edu.uerj.adapters.api.dto.OrderDTO
import edu.uerj.adapters.api.dto.PaginatedResult
import edu.uerj.domain.Order
import edu.uerj.domain.enums.OrderStatus
import edu.uerj.services.OrderService
import jakarta.inject.Inject
import jakarta.ws.rs.GET
import jakarta.ws.rs.PATCH
import jakarta.ws.rs.Path
import jakarta.ws.rs.PathParam
import jakarta.ws.rs.Produces
import jakarta.ws.rs.QueryParam
import jakarta.ws.rs.core.MediaType
import jakarta.ws.rs.core.Response
import org.eclipse.microprofile.openapi.annotations.media.Content
import org.eclipse.microprofile.openapi.annotations.media.Schema
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses

@Path("/v1/order")
class OrderController
    @Inject
    constructor(
        private val orderService: OrderService,
    ) {
        @Path("/{orderId}")
        @APIResponses(
            value = [
                APIResponse(
                    responseCode = "200",
                    description = "returned find Order",
                    content = [Content(schema = Schema(implementation = OrderDTO::class))],
                ),
                APIResponse(
                    responseCode = "404",
                    description = "Order not found",
                    content = [Content(schema = Schema(implementation = ErrorDTO::class))],
                ),
                APIResponse(
                    responseCode = "500",
                    description = "Internal server error",
                    content = [Content(schema = Schema(implementation = ErrorDTO::class))],
                ),
            ],
        )
        @GET
        @Produces(MediaType.APPLICATION_JSON)
        fun getOrderById(
            @PathParam("orderId") orderId: String,
        ): Response {
            try {
                val order =
                    orderService.getOrder(orderId.toLong()) ?: return Response
                        .status(Response.Status.NOT_FOUND)
                        .entity(ErrorDTO("Order with id $orderId not found"))
                        .build()

                val dto = OrderDTO(order)
                return Response.status(Response.Status.OK).entity(dto).build()
            } catch (exception: Exception) {
                return Response
                    .status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(ErrorDTO(exception.message))
                    .build()
            }
        }


        @Path("/list")
        @APIResponses(
            value = [
                APIResponse(
                    responseCode = "200",
                    description = "returned list Orders",
                    content = [Content(schema = Schema(implementation = PaginatedResult::class))],
                ),
                APIResponse(
                    responseCode = "400",
                    description = "Error list orders",
                    content = [Content(schema = Schema(implementation = PaginatedResult::class))],
                ),
                APIResponse(
                    responseCode = "500",
                    description = "Internal server error",
                    content = [Content(schema = Schema(implementation = ErrorDTO::class))],
                ),
            ],
        )
        @GET
        @Produces(MediaType.APPLICATION_JSON)
        fun listOrders(
            @QueryParam("limitPerPage") limit: Long,
            @QueryParam("page") offset: Long = 0,
        ): Response {
            try {
                var paginatedOrders = orderService.listAll(limit, offset)

                if (paginatedOrders.items == null) {
                    return Response
                        .status(Response.Status.BAD_REQUEST)
                        .entity(PaginatedResult<OrderDTO>())
                        .build()
                }

                val paginedResult =
                    PaginatedResult(
                        items = paginatedOrders.items!!.map { OrderDTO(it) } as MutableList<OrderDTO>,
                        limit = paginatedOrders.limit,
                        total = paginatedOrders.total,
                    )
                return Response
                    .status(Response.Status.OK)
                    .entity(paginedResult)
                    .build()
            } catch (exception: Exception) {
                return Response
                    .status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(ErrorDTO(exception.message))
                    .build()
            }
        }

        @Path("/{orderId}/delivery/{deliveryId}/{newStatus}")
        @APIResponses(
            value = [
                APIResponse(
                    responseCode = "202",
                    description = "returned updated Order",
                    content = [Content(schema = Schema(implementation = OrderDTO::class))],
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
            @PathParam("newStatus") status: OrderStatus): Response {
            try {
                val order = orderService.updateStatus(orderId, deliveryId, status)
                if (order == null) {
                    return Response
                        .status(Response.Status.NOT_FOUND)
                        .entity(null)
                        .build()
                }

                return Response
                    .status(Response.Status.ACCEPTED)
                    .entity(OrderDTO(order))
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
