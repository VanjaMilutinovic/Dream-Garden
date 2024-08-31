import { Photo } from "./photo.model";
import { UserStatus } from "./user-status.model";
import { UserType } from "./user-type.model";

export class User {
  userId: number = 0;
  userTypeId: UserType = new UserType();
  username: string = "";
  hashedPassword: string = "";
  name: string = "";
  lastname: string = "";
  gender: 'M' | 'F' = `M`;
  address: string = "";
  contactNumber: string = "";
  email: string = "";
  photoId: Photo = new Photo();
  creditCardNumber?: string;
  userStatusId: UserStatus = new UserStatus();
}
  