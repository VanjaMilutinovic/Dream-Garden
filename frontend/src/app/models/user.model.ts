export class User {
  user_id: number = 0;
  user_type_id: number = 1;
  username: string = "";
  hashed_password: string = "";
  name: string = "";
  lastname: string = "";
  gender: 'M' | 'F' = `M`;
  address: string = "";
  contact_number: string = "";
  email: string = "";
  photo_id: number = 1;
  credit_card_number?: string;
  user_status_id: number = 1;
}
  