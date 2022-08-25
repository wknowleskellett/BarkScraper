package dev.williamknowleskellett.barkscraper;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import net.minecraft.block.*;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.Util;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.WorldView;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.Function;

public class BarkBlock extends Block implements Waterloggable {
    private static final BooleanProperty WATERLOGGED = Properties.WATERLOGGED;
    private static final float field_31194 = 1.0f;
    private static final VoxelShape UP_SHAPE = Block.createCuboidShape(0.0, 15.0, 0.0, 16.0, 16.0, 16.0);
    private static final VoxelShape DOWN_SHAPE = Block.createCuboidShape(0.0, 0.0, 0.0, 16.0, 1.0, 16.0);
    private static final VoxelShape EAST_SHAPE = Block.createCuboidShape(0.0, 0.0, 0.0, 1.0, 16.0, 16.0);
    private static final VoxelShape WEST_SHAPE = Block.createCuboidShape(15.0, 0.0, 0.0, 16.0, 16.0, 16.0);
    private static final VoxelShape SOUTH_SHAPE = Block.createCuboidShape(0.0, 0.0, 0.0, 16.0, 16.0, 1.0);
    private static final VoxelShape NORTH_SHAPE = Block.createCuboidShape(0.0, 0.0, 15.0, 16.0, 16.0, 16.0);
    private static final Map<Direction, BooleanProperty> FACING_PROPERTIES = ConnectingBlock.FACING_PROPERTIES;
    private static final Map<Direction, VoxelShape> SHAPES_FOR_DIRECTIONS = Util.make(Maps.newEnumMap(Direction.class), shapes -> {
        shapes.put(Direction.NORTH, SOUTH_SHAPE);
        shapes.put(Direction.EAST, WEST_SHAPE);
        shapes.put(Direction.SOUTH, NORTH_SHAPE);
        shapes.put(Direction.WEST, EAST_SHAPE);
        shapes.put(Direction.UP, UP_SHAPE);
        shapes.put(Direction.DOWN, DOWN_SHAPE);
    });
    protected static final Direction[] DIRECTIONS = Direction.values();
    private final ImmutableMap<BlockState, VoxelShape> SHAPES;
    private final boolean hasAllHorizontalDirections;
    private final boolean canMirrorX;
    private final boolean canMirrorZ;

    public BarkBlock(AbstractBlock.Settings settings) {
        super(settings);
        this.setDefaultState(BarkBlock.withAllDirections(this.stateManager).with(WATERLOGGED, false));
        this.SHAPES = this.getShapesForStates(BarkBlock::getShapeForState);
        this.hasAllHorizontalDirections = Direction.Type.HORIZONTAL.stream().allMatch(this::canHaveDirection);
        this.canMirrorX = Direction.Type.HORIZONTAL.stream().filter(Direction.Axis.X).filter(this::canHaveDirection).count() % 2L == 0L;
        this.canMirrorZ = Direction.Type.HORIZONTAL.stream().filter(Direction.Axis.Z).filter(this::canHaveDirection).count() % 2L == 0L;
    }

    protected boolean canHaveDirection(Direction direction) {
        return true;
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        for (Direction direction : DIRECTIONS) {
            if (!this.canHaveDirection(direction)) continue;
            builder.add(BarkBlock.getProperty(direction));
        }
        builder.add(WATERLOGGED);
    }

    @Override
    public BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState neighborState, WorldAccess world, BlockPos pos, BlockPos neighborPos) {
        if (state.get(WATERLOGGED).booleanValue()) {
            world.createAndScheduleFluidTick(pos, Fluids.WATER, Fluids.WATER.getTickRate(world));
        }
        if (!BarkBlock.hasAnyDirection(state)) {
            return Blocks.AIR.getDefaultState();
        }
        if (!BarkBlock.hasDirection(state, direction) || BarkBlock.canPlaceOn(world, direction, neighborPos, neighborState)) {
            return state;
        }
        return BarkBlock.disableDirection(state, BarkBlock.getProperty(direction));
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return this.SHAPES.get(state);
    }

    @Override
    public boolean canPlaceAt(BlockState state, WorldView world, BlockPos pos) {
        boolean bl = false;
        for (Direction direction : DIRECTIONS) {
            if (!BarkBlock.hasDirection(state, direction)) continue;
            BlockPos blockPos = pos.offset(direction);
            if (!BarkBlock.canPlaceOn(world, direction, blockPos, world.getBlockState(blockPos))) {
                return false;
            }
            bl = true;
        }
        return bl;
    }

    @Override
    public boolean canReplace(BlockState state, ItemPlacementContext context) {
        return context.getStack().isOf(state.getBlock().asItem()) && BarkBlock.isNotFullBlock(state);
    }

    @Override
    @Nullable
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        World world = ctx.getWorld();
        BlockPos blockPos = ctx.getBlockPos();
        BlockState blockState = world.getBlockState(blockPos);
        return Arrays.stream(ctx.getPlacementDirections()).map(direction -> this.withDirection(blockState, world, blockPos, (Direction)direction)).filter(Objects::nonNull).findFirst().orElse(null);
    }

    @Nullable
    public BlockState withDirection(BlockState state, BlockView world, BlockPos pos, Direction direction) {
        BlockState blockState;
        if (!this.canHaveDirection(direction)) {
            return null;
        }
        if (state.isOf(this)) {
            if (BarkBlock.hasDirection(state, direction)) {
                return null;
            }
            blockState = state;
        } else {
            blockState = this.isWaterlogged() && state.getFluidState().isEqualAndStill(Fluids.WATER) ? (BlockState)this.getDefaultState().with(Properties.WATERLOGGED, true) : this.getDefaultState();
        }
        BlockPos blockPos = pos.offset(direction);
        if (BarkBlock.canPlaceOn(world, direction, blockPos, world.getBlockState(blockPos))) {
            return (BlockState)blockState.with(BarkBlock.getProperty(direction), true);
        }
        return null;
    }

//    @Override
//    public BlockState rotate(BlockState state, BlockRotation rotation) {
//        if (!this.hasAllHorizontalDirections) {
//            return state;
//        }
//        return this.mirror(state, rotation::rotate);
//    }

//    @Override
//    public BlockState mirror(BlockState state, BlockMirror mirror) {
//        if (mirror == BlockMirror.FRONT_BACK && !this.canMirrorX) {
//            return state;
//        }
//        if (mirror == BlockMirror.LEFT_RIGHT && !this.canMirrorZ) {
//            return state;
//        }
//        return this.mirror(state, mirror::apply);
//    }

    private BlockState mirror(BlockState state, Function<Direction, Direction> mirror) {
        BlockState blockState = state;
        for (Direction direction : DIRECTIONS) {
            if (!this.canHaveDirection(direction)) continue;
            blockState = (BlockState)blockState.with(BarkBlock.getProperty(mirror.apply(direction)), state.get(BarkBlock.getProperty(direction)));
        }
        return blockState;
    }

//    public boolean trySpreadRandomly(BlockState state, ServerWorld world, BlockPos pos, Random random) {
//        ArrayList<Direction> list = Lists.newArrayList(DIRECTIONS);
//        Collections.shuffle(list);
//        return list.stream().filter(from -> BarkBlock.hasDirection(state, from)).anyMatch(to -> this.trySpreadRandomly(state, world, pos, (Direction)to, random, false));
//    }

//    public boolean trySpreadRandomly(BlockState state, WorldAccess world, BlockPos pos, Direction from, Random random, boolean postProcess) {
//        List<Direction> list = Arrays.asList(DIRECTIONS);
//        Collections.shuffle(list, random);
//        return list.stream().anyMatch(to -> this.trySpreadTo(state, world, pos, from, (Direction)to, postProcess));
//    }

//    public boolean trySpreadTo(BlockState state, WorldAccess world, BlockPos pos, Direction from, Direction to, boolean postProcess) {
//        Optional<Pair<BlockPos, Direction>> optional = this.getSpreadLocation(state, world, pos, from, to);
//        if (optional.isPresent()) {
//            Pair<BlockPos, Direction> pair = optional.get();
//            return this.addDirection(world, pair.getFirst(), pair.getSecond(), postProcess);
//        }
//        return false;
//    }

//    protected boolean canSpread(BlockState state, BlockView world, BlockPos pos, Direction from) {
//        return Stream.of(DIRECTIONS).anyMatch(to -> this.getSpreadLocation(state, world, pos, from, (Direction)to).isPresent());
//    }

//    private Optional<Pair<BlockPos, Direction>> getSpreadLocation(BlockState state, BlockView world, BlockPos pos, Direction from, Direction to) {
//        Direction direction;
//        if (to.getAxis() == from.getAxis() || !BarkBlock.hasDirection(state, from) || BarkBlock.hasDirection(state, to)) {
//            return Optional.empty();
//        }
//        if (this.canSpreadTo(world, pos, to)) {
//            return Optional.of(Pair.of(pos, to));
//        }
//        BlockPos blockPos = pos.offset(to);
//        if (this.canSpreadTo(world, blockPos, from)) {
//            return Optional.of(Pair.of(blockPos, from));
//        }
//        BlockPos blockPos2 = blockPos.offset(from);
//        if (this.canSpreadTo(world, blockPos2, direction = to.getOpposite())) {
//            return Optional.of(Pair.of(blockPos2, direction));
//        }
//        return Optional.empty();
//    }

//    private boolean canSpreadTo(BlockView world, BlockPos pos, Direction direction) {
//        BlockState blockState = world.getBlockState(pos);
//        if (!this.canGrowIn(blockState)) {
//            return false;
//        }
//        BlockState blockState2 = this.withDirection(blockState, world, pos, direction);
//        return blockState2 != null;
//    }

    private boolean addDirection(WorldAccess world, BlockPos pos, Direction direction, boolean postProcess) {
        BlockState blockState = world.getBlockState(pos);
        BlockState blockState2 = this.withDirection(blockState, world, pos, direction);
        if (blockState2 != null) {
            if (postProcess) {
                world.getChunk(pos).markBlockForPostProcessing(pos);
            }
            return world.setBlockState(pos, blockState2, Block.NOTIFY_LISTENERS);
        }
        return false;
    }

    private static boolean hasDirection(BlockState state, Direction direction) {
        BooleanProperty booleanProperty = BarkBlock.getProperty(direction);
        return state.contains(booleanProperty) && state.get(booleanProperty) != false;
    }

    private static boolean canPlaceOn(BlockView world, Direction direction, BlockPos pos, BlockState state) {
        return true; //Block.isFaceFullSquare(state.getCollisionShape(world, pos), direction.getOpposite());
    }

    private boolean isWaterlogged() {
        return this.stateManager.getProperties().contains(Properties.WATERLOGGED);
    }

    private static BlockState disableDirection(BlockState state, BooleanProperty direction) {
        BlockState blockState = (BlockState)state.with(direction, false);
        if (BarkBlock.hasAnyDirection(blockState)) {
            return blockState;
        }
        return Blocks.AIR.getDefaultState();
    }

    public static BooleanProperty getProperty(Direction direction) {
        return FACING_PROPERTIES.get(direction);
    }

    private static BlockState withAllDirections(StateManager<Block, BlockState> stateManager) {
        BlockState blockState = stateManager.getDefaultState();
        for (BooleanProperty booleanProperty : FACING_PROPERTIES.values()) {
            if (!blockState.contains(booleanProperty)) continue;
            blockState = (BlockState)blockState.with(booleanProperty, false);
        }
        return blockState;
    }

    private static VoxelShape getShapeForState(BlockState state) {
        VoxelShape voxelShape = VoxelShapes.empty();
        for (Direction direction : DIRECTIONS) {
            if (!BarkBlock.hasDirection(state, direction)) continue;
            voxelShape = VoxelShapes.union(voxelShape, SHAPES_FOR_DIRECTIONS.get(direction));
        }
        return voxelShape.isEmpty() ? VoxelShapes.fullCube() : voxelShape;
    }

    protected static boolean hasAnyDirection(BlockState state) {
        return Arrays.stream(DIRECTIONS).anyMatch(direction -> BarkBlock.hasDirection(state, direction));
    }

    private static boolean isNotFullBlock(BlockState state) {
        return Arrays.stream(DIRECTIONS).anyMatch(direction -> !BarkBlock.hasDirection(state, direction));
    }

    @Override
    public FluidState getFluidState(BlockState state) {
        if (state.get(WATERLOGGED).booleanValue()) {
            return Fluids.WATER.getStill(false);
        }
        return super.getFluidState(state);
    }

    @Override
    public boolean isTranslucent(BlockState state, BlockView world, BlockPos pos) {
        return state.getFluidState().isEmpty();
    }
}
