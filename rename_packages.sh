#!/bin/bash

# Function to move and update package declarations
move_and_update_package() {
    local src_file=$1
    local dest_dir=$2
    local package_name=$3
    
    # Create destination directory if it doesn't exist
    mkdir -p "$dest_dir"
    
    # Get the filename
    local filename=$(basename "$src_file")
    
    # Move the file
    mv "$src_file" "$dest_dir/$filename"
    
    # Update package declaration
    sed -i '' "s/^package .*$/package $package_name/" "$dest_dir/$filename"
    
    # Update imports
    find "$dest_dir" -name "*.kt" -type f -exec sed -i '' "s/import learn\.ai\.ecommerce/import com.ecommerce/g" {} \;
}

# Move and update files
move_and_update_package \
    "src/main/kotlin/learn/ai/ecommerce/EcommerceApplication.kt" \
    "src/main/kotlin/com/ecommerce" \
    "com.ecommerce"

move_and_update_package \
    "src/main/kotlin/learn/ai/ecommerce/config/RedisConfig.kt" \
    "src/main/kotlin/com/ecommerce/config" \
    "com.ecommerce.config"

move_and_update_package \
    "src/main/kotlin/learn/ai/ecommerce/controller/HealthController.kt" \
    "src/main/kotlin/com/ecommerce/controller" \
    "com.ecommerce.controller"

move_and_update_package \
    "src/main/kotlin/learn/ai/ecommerce/controller/UserController.kt" \
    "src/main/kotlin/com/ecommerce/controller" \
    "com.ecommerce.controller"

move_and_update_package \
    "src/main/kotlin/learn/ai/ecommerce/db/DatabaseClient.kt" \
    "src/main/kotlin/com/ecommerce/db" \
    "com.ecommerce.db"

move_and_update_package \
    "src/main/kotlin/learn/ai/ecommerce/dto/UserDto.kt" \
    "src/main/kotlin/com/ecommerce/dto" \
    "com.ecommerce.dto"

move_and_update_package \
    "src/main/kotlin/learn/ai/ecommerce/exception/BusinessException.kt" \
    "src/main/kotlin/com/ecommerce/exception" \
    "com.ecommerce.exception"

move_and_update_package \
    "src/main/kotlin/learn/ai/ecommerce/exception/ErrorCode.kt" \
    "src/main/kotlin/com/ecommerce/exception" \
    "com.ecommerce.exception"

move_and_update_package \
    "src/main/kotlin/learn/ai/ecommerce/exception/GlobalExceptionHandler.kt" \
    "src/main/kotlin/com/ecommerce/exception" \
    "com.ecommerce.exception"

move_and_update_package \
    "src/main/kotlin/learn/ai/ecommerce/model/User.kt" \
    "src/main/kotlin/com/ecommerce/model" \
    "com.ecommerce.model"

move_and_update_package \
    "src/main/kotlin/learn/ai/ecommerce/redis/RedisClient.kt" \
    "src/main/kotlin/com/ecommerce/redis" \
    "com.ecommerce.redis"

move_and_update_package \
    "src/main/kotlin/learn/ai/ecommerce/repository/BaseRepository.kt" \
    "src/main/kotlin/com/ecommerce/repository" \
    "com.ecommerce.repository"

move_and_update_package \
    "src/main/kotlin/learn/ai/ecommerce/repository/UserRepository.kt" \
    "src/main/kotlin/com/ecommerce/repository" \
    "com.ecommerce.repository"

move_and_update_package \
    "src/main/kotlin/learn/ai/ecommerce/service/HealthService.kt" \
    "src/main/kotlin/com/ecommerce/service" \
    "com.ecommerce.service"

move_and_update_package \
    "src/main/kotlin/learn/ai/ecommerce/service/LockService.kt" \
    "src/main/kotlin/com/ecommerce/service" \
    "com.ecommerce.service"

move_and_update_package \
    "src/main/kotlin/learn/ai/ecommerce/service/UserService.kt" \
    "src/main/kotlin/com/ecommerce/service" \
    "com.ecommerce.service"

move_and_update_package \
    "src/main/kotlin/learn/ai/ecommerce/service/impl/HealthServiceImpl.kt" \
    "src/main/kotlin/com/ecommerce/service/impl" \
    "com.ecommerce.service.impl"

# Remove the old directory structure
rm -rf src/main/kotlin/learn

# Update any remaining imports in test files if they exist
find "src/test/kotlin" -type f -name "*.kt" -exec sed -i '' "s/import learn\.ai\.ecommerce/import com.ecommerce/g" {} \;

echo "Project structure has been reorganized successfully!"
